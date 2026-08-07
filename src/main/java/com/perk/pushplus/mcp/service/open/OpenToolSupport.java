package com.perk.pushplus.mcp.service.open;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.perk.pushplus.mcp.client.OpenApiClient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

abstract class OpenToolSupport {

    protected final OpenApiClient openApiClient;

    protected OpenToolSupport(OpenApiClient openApiClient) {
        this.openApiClient = openApiClient;
    }

    protected String run(Supplier<String> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return "请求失败:" + e.getMessage();
        }
    }

    protected Map<String, Object> query(Object... kv) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i + 1 < kv.length; i += 2) {
            map.put(String.valueOf(kv[i]), kv[i + 1]);
        }
        return map;
    }

    protected JSONObject pageBody(Integer current, Integer pageSize, Object params) {
        JSONObject body = new JSONObject();
        body.set("current", current == null ? 1 : current);
        body.set("pageSize", pageSize == null ? 20 : pageSize);
        if (params != null) {
            body.set("params", params instanceof String ? JSONUtil.parseObj((String) params) : params);
        }
        return body;
    }
}
