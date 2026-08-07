package com.perk.pushplus.mcp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "pushplus")
public class PushplusProperties {

    /**
     * API 根地址
     */
    private String baseUrl = "https://www.pushplus.plus";

    /**
     * 发送接口，未配置时由 baseUrl + /send 推导
     */
    private String url;

    /**
     * 批量发送接口，未配置时由 baseUrl + /batchSend 推导
     */
    private String batchUrl;

    /**
     * 用户 token（发送与开放接口 getAccessKey 共用）
     */
    private String token;

    /**
     * 开放接口 secretKey
     */
    private String secretKey;

    /**
     * 开放接口 API 前缀（官网文档为 /api）
     */
    private String openApiPrefix = "/api";

    public String getBaseUrl() {
        if (baseUrl == null || baseUrl.isBlank()) {
            return "https://www.pushplus.plus";
        }
        return baseUrl.replaceAll("/+$", "");
    }

    /**
     * 开放接口根地址，如 https://www.pushplus.plus/api
     */
    public String getOpenApiBaseUrl() {
        String prefix = openApiPrefix == null ? "/api" : openApiPrefix.trim();
        if (prefix.isEmpty()) {
            return getBaseUrl();
        }
        if (!prefix.startsWith("/")) {
            prefix = "/" + prefix;
        }
        return getBaseUrl() + prefix.replaceAll("/+$", "");
    }

    public String getUrl() {
        if (url != null && !url.isBlank()) {
            return url;
        }
        return getBaseUrl() + "/send";
    }

    public String getBatchUrl() {
        if (batchUrl != null && !batchUrl.isBlank()) {
            return batchUrl;
        }
        return getBaseUrl() + "/batchSend";
    }

    public boolean hasOpenCredentials() {
        return token != null && !token.isBlank()
                && secretKey != null && !secretKey.isBlank();
    }
}
