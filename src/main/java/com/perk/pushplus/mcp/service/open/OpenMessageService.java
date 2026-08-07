package com.perk.pushplus.mcp.service.open;

import cn.hutool.json.JSONObject;
import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class OpenMessageService extends OpenToolSupport {

    public OpenMessageService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "POST /open/message/list - 分页查询消息列表。"
            + OpenApiDocs.PAGE_REQ + OpenApiDocs.RESULT + OpenApiDocs.PAGE_RESP
            + "list项: channel(wechat/mail/cp/webhook), messageType(1一对一/2一对多),"
            + "shortCode(短链码可查发送结果), title, topicName(一对多才有), updateTime。")
    public String openMessageList(
            @ToolParam(description = "当前所在分页数，默认1", required = false) Integer current,
            @ToolParam(description = "每页大小，默认20，最大50", required = false) Integer pageSize,
            @ToolParam(description = "是否已读（扩展筛选）", required = false) Integer isRead,
            @ToolParam(description = "发送渠道；wechat/mail/cp/webhook", required = false) String channel,
            @ToolParam(description = "消息类型；1-一对一，2-一对多", required = false) Integer messageType) {
        JSONObject params = new JSONObject();
        params.set("isRead", isRead);
        params.set("channel", channel);
        params.set("messageType", messageType);
        return run(() -> openApiClient.post("/open/message/list", pageBody(current, pageSize, params)));
    }

    @Tool(description = "GET /open/message/sendMessageResult - 查询消息发送结果。"
            + OpenApiDocs.RESULT
            + "data: status(0未投递/1发送中/2已发送/3发送失败), errorMessage(失败原因), updateTime。")
    public String openMessageSendResult(
            @ToolParam(description = "消息短链码；发送消息同步返回的短链码") String shortCode) {
        return run(() -> openApiClient.get("/open/message/sendMessageResult", query("shortCode", shortCode)));
    }

    @Tool(description = "DELETE /open/message/deleteMessage - 高风险：删除后所有接收人均无法查看且无法撤销。"
            + OpenApiDocs.RESULT)
    public String openMessageDelete(
            @ToolParam(description = "消息短链码") String shortCode) {
        return run(() -> openApiClient.delete("/open/message/deleteMessage", query("shortCode", shortCode)));
    }
}
