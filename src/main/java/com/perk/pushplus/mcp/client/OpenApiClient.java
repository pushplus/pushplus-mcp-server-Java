package com.perk.pushplus.mcp.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.perk.pushplus.mcp.properties.PushplusProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class OpenApiClient {

    private static final int DEFAULT_TIMEOUT_MS = 15000;

    private final PushplusProperties properties;
    private final ReentrantLock lock = new ReentrantLock();

    private volatile String accessKey;
    private volatile long expireAt;

    public OpenApiClient(PushplusProperties properties) {
        this.properties = properties;
    }

    public boolean hasCredentials() {
        return properties.hasOpenCredentials();
    }

    public String getAccessKey(boolean force) {
        ensureCredentials();
        if (!force && StringUtils.hasText(accessKey) && System.currentTimeMillis() < expireAt) {
            return accessKey;
        }
        lock.lock();
        try {
            if (!force && StringUtils.hasText(accessKey) && System.currentTimeMillis() < expireAt) {
                return accessKey;
            }
            return refreshAccessKey();
        } finally {
            lock.unlock();
        }
    }

    private String refreshAccessKey() {
        String url = properties.getOpenApiBaseUrl() + "/common/openApi/getAccessKey";
        JSONObject body = new JSONObject();
        body.set("token", properties.getToken());
        body.set("secretKey", properties.getSecretKey());

        HttpResponse response = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(body.toString())
                .timeout(DEFAULT_TIMEOUT_MS)
                .execute();

        String responseBody = response.body();
        ensureJsonResponse(responseBody, url);

        JSONObject result = JSONUtil.parseObj(responseBody);
        if (result.getInt("code", -1) != 200) {
            throw new IllegalStateException("获取 access-key 失败: " + result.getStr("msg", "未知错误")
                    + "（请确认 PUSHPLUS_TOKEN 为用户token、已开启开放接口，且出口 IP 在白名单内）");
        }
        JSONObject data = result.getJSONObject("data");
        if (data == null || !StringUtils.hasText(data.getStr("accessKey"))) {
            throw new IllegalStateException("获取 access-key 失败: 响应无 accessKey");
        }
        this.accessKey = data.getStr("accessKey");
        long expiresIn = data.getLong("expiresIn", 7200L);
        this.expireAt = System.currentTimeMillis() + Math.max(60L, expiresIn - 60L) * 1000L;
        return this.accessKey;
    }

    public String request(String method, String path, Map<String, Object> query, Object body, int timeoutMs) {
        return request(method, path, query, body, timeoutMs, false);
    }

    private String request(String method, String path, Map<String, Object> query, Object body,
                           int timeoutMs, boolean retried) {
        ensureCredentials();
        String key = getAccessKey(false);
        String url = buildUrl(path, query);

        HttpRequest request = createRequest(method, url)
                .header("access-key", key)
                .timeout(timeoutMs > 0 ? timeoutMs : DEFAULT_TIMEOUT_MS);

        if (body != null) {
            request.header("Content-Type", "application/json");
            request.body(body instanceof String ? (String) body : JSONUtil.toJsonStr(body));
        }

        HttpResponse response = request.execute();
        String responseBody = response.body();
        ensureJsonResponse(responseBody, url);

        if (isUnauthorized(response.getStatus(), responseBody) && !retried) {
            getAccessKey(true);
            return request(method, path, query, body, timeoutMs, true);
        }
        return responseBody;
    }

    public String get(String path, Map<String, Object> query) {
        return request("GET", path, query, null, DEFAULT_TIMEOUT_MS);
    }

    public String get(String path, Map<String, Object> query, int timeoutMs) {
        return request("GET", path, query, null, timeoutMs);
    }

    public String post(String path, Object body) {
        return request("POST", path, null, body, DEFAULT_TIMEOUT_MS);
    }

    public String post(String path, Map<String, Object> query, Object body) {
        return request("POST", path, query, body, DEFAULT_TIMEOUT_MS);
    }

    public String delete(String path, Map<String, Object> query) {
        return request("DELETE", path, query, null, DEFAULT_TIMEOUT_MS);
    }

    public String uploadImage(String filename, String contentBase64) {
        ensureCredentials();
        String key = getAccessKey(false);
        byte[] bytes = java.util.Base64.getDecoder().decode(contentBase64.getBytes(StandardCharsets.UTF_8));
        File temp = null;
        String uploadUrl = properties.getOpenApiBaseUrl() + "/open/file/uploadImage";
        try {
            temp = File.createTempFile("pushplus-upload-", "-" + sanitizeFilename(filename));
            java.nio.file.Files.write(temp.toPath(), bytes);

            HttpResponse response = HttpRequest.post(uploadUrl)
                    .header("access-key", key)
                    .form("file", temp)
                    .timeout(30000)
                    .execute();
            String responseBody = response.body();
            ensureJsonResponse(responseBody, uploadUrl);
            if (isUnauthorized(response.getStatus(), responseBody)) {
                getAccessKey(true);
                response = HttpRequest.post(uploadUrl)
                        .header("access-key", getAccessKey(false))
                        .form("file", temp)
                        .timeout(30000)
                        .execute();
                responseBody = response.body();
                ensureJsonResponse(responseBody, uploadUrl);
            }
            return responseBody;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("上传图片失败: " + e.getMessage(), e);
        } finally {
            if (temp != null && temp.exists()) {
                //noinspection ResultOfMethodCallIgnored
                temp.delete();
            }
        }
    }

    public String accessKeyInfoJson(boolean force) {
        String key = getAccessKey(force);
        JSONObject obj = new JSONObject();
        obj.set("accessKey", key);
        obj.set("expiresIn", Math.max(0, (expireAt - System.currentTimeMillis()) / 1000));
        return obj.toStringPretty();
    }

    private void ensureCredentials() {
        if (!hasCredentials()) {
            throw new IllegalStateException("缺少开放接口凭证，请配置 PUSHPLUS_TOKEN 与 PUSHPLUS_SECRET_KEY");
        }
    }

    /**
     * 开放接口必须返回 JSON。若误请求到官网前端（缺 /api 前缀），会返回 HTML，
     * Hutool 解析时可能抛出 Mismatched link and head。
     */
    private void ensureJsonResponse(String responseBody, String url) {
        if (!StringUtils.hasText(responseBody)) {
            throw new IllegalStateException("Open API 响应为空: " + url);
        }
        String trimmed = responseBody.trim();
        if (trimmed.startsWith("<") || trimmed.toLowerCase().startsWith("<!doctype")) {
            throw new IllegalStateException(
                    "Open API 返回了 HTML 而非 JSON，请确认开放接口地址使用 /api 前缀。请求URL: " + url
                            + "；当前 openApiBaseUrl=" + properties.getOpenApiBaseUrl());
        }
    }

    private boolean isUnauthorized(int status, String body) {
        if (status == 401) {
            return true;
        }
        if (!StringUtils.hasText(body)) {
            return false;
        }
        try {
            JSONObject json = JSONUtil.parseObj(body);
            Integer code = json.getInt("code");
            String msg = json.getStr("msg", "");
            return (code != null && code == 401)
                    || code != null && code == 302
                    || msg.toLowerCase().contains("unauthorized")
                    || msg.contains("未授权")
                    || msg.contains("未登录")
                    || msg.contains("access-key")
                    || msg.contains("令牌无效");
        } catch (Exception ignored) {
            return false;
        }
    }

    private String buildUrl(String path, Map<String, Object> query) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        StringBuilder sb = new StringBuilder(properties.getOpenApiBaseUrl()).append(normalized);
        if (query != null && !query.isEmpty()) {
            boolean first = true;
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                String value = String.valueOf(entry.getValue());
                if (!StringUtils.hasText(value)) {
                    continue;
                }
                sb.append(first ? '?' : '&');
                first = false;
                sb.append(entry.getKey()).append('=')
                        .append(java.net.URLEncoder.encode(value, StandardCharsets.UTF_8));
            }
        }
        return sb.toString();
    }

    private HttpRequest createRequest(String method, String url) {
        return switch (method.toUpperCase()) {
            case "POST" -> HttpRequest.post(url);
            case "PUT" -> HttpRequest.put(url);
            case "DELETE" -> HttpRequest.delete(url);
            default -> HttpRequest.get(url);
        };
    }

    private String sanitizeFilename(String filename) {
        if (!StringUtils.hasText(filename)) {
            return "image.bin";
        }
        return filename.replaceAll("[\\\\/]+", "_");
    }
}
