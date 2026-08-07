package com.perk.pushplus.mcp.service.open;

import cn.hutool.json.JSONObject;
import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class OpenWebhookService extends OpenToolSupport {

    public OpenWebhookService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "POST /open/webhook/add - 新增webhook。必填: webhookCode/Name/Type/Url；"
            + "自定义类型(12)可填httpMethod/headers/body。" + OpenApiDocs.WEBHOOK_TYPE + "。"
            + OpenApiDocs.RESULT + "data: 新建webhook编号。")
    public String openWebhookAdd(
            @ToolParam(description = "webhook名称") String webhookName,
            @ToolParam(description = "webhook编码") String webhookCode,
            @ToolParam(description = "调用的url地址") String webhookUrl,
            @ToolParam(description = OpenApiDocs.WEBHOOK_TYPE) Integer webhookType,
            @ToolParam(description = "请求方法（仅自定义类型）", required = false) String httpMethod,
            @ToolParam(description = "body内容（仅自定义类型）", required = false) String body,
            @ToolParam(description = "请求头（仅自定义类型）", required = false) String headers) {
        JSONObject json = new JSONObject();
        json.set("webhookName", webhookName);
        json.set("webhookCode", webhookCode);
        json.set("webhookUrl", webhookUrl);
        json.set("webhookType", webhookType);
        json.set("httpMethod", httpMethod);
        json.set("body", body);
        json.set("headers", headers);
        return run(() -> openApiClient.post("/open/webhook/add", json));
    }

    @Tool(description = "POST /open/webhook/list - webhook列表。"
            + OpenApiDocs.PAGE_REQ + OpenApiDocs.RESULT + OpenApiDocs.PAGE_RESP
            + "list项: id,webhookCode,webhookName,webhookType,webhookTypeName,webhookUrl,createTime。"
            + OpenApiDocs.WEBHOOK_TYPE)
    public String openWebhookList(
            @ToolParam(description = "当前所在分页数，默认1", required = false) Integer current,
            @ToolParam(description = "每页大小，默认20，最大50", required = false) Integer pageSize) {
        return run(() -> openApiClient.post("/open/webhook/list", pageBody(current, pageSize, null)));
    }

    @Tool(description = "GET /open/webhook/detail - webhook详情。自定义类型额外返回httpMethod/headers/body。"
            + OpenApiDocs.RESULT + OpenApiDocs.WEBHOOK_TYPE)
    public String openWebhookDetail(@ToolParam(description = "webhook编号") Long webhookId) {
        return run(() -> openApiClient.get("/open/webhook/detail", query("webhookId", webhookId)));
    }

    @Tool(description = "GET /open/webhook/delete - 高风险：删除webhook。" + OpenApiDocs.RESULT)
    public String openWebhookDelete(@ToolParam(description = "webhook编号") Long webhookId) {
        return run(() -> openApiClient.get("/open/webhook/delete", query("webhookId", webhookId)));
    }

    @Tool(description = "POST /open/webhook/edit - 修改webhook。必填: id,webhookCode,webhookName,webhookType,webhookUrl。"
            + OpenApiDocs.WEBHOOK_TYPE + "。" + OpenApiDocs.RESULT + "data: 如修改成功。")
    public String openWebhookEdit(
            @ToolParam(description = "webhook编号") Long id,
            @ToolParam(description = "webhook名称") String webhookName,
            @ToolParam(description = "webhook编码") String webhookCode,
            @ToolParam(description = "调用的url地址") String webhookUrl,
            @ToolParam(description = OpenApiDocs.WEBHOOK_TYPE) Integer webhookType,
            @ToolParam(description = "请求方法（仅自定义类型）", required = false) String httpMethod,
            @ToolParam(description = "body内容（仅自定义类型）", required = false) String body,
            @ToolParam(description = "请求头（仅自定义类型）", required = false) String headers) {
        JSONObject json = new JSONObject();
        json.set("id", id);
        json.set("webhookName", webhookName);
        json.set("webhookCode", webhookCode);
        json.set("webhookUrl", webhookUrl);
        json.set("webhookType", webhookType);
        json.set("httpMethod", httpMethod);
        json.set("body", body);
        json.set("headers", headers);
        return run(() -> openApiClient.post("/open/webhook/edit", json));
    }
}
