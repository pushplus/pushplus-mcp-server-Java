package com.perk.pushplus.mcp.service.open;

import cn.hutool.json.JSONObject;
import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class OpenTokenService extends OpenToolSupport {

    public OpenTokenService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "POST /open/token/list - 消息token列表。"
            + OpenApiDocs.PAGE_REQ + OpenApiDocs.RESULT + OpenApiDocs.PAGE_RESP
            + "list项: id(编号), name(令牌名称), expireTime(过期时间), token(消息token)。")
    public String openTokenList(
            @ToolParam(description = "当前所在分页数，默认1", required = false) Integer current,
            @ToolParam(description = "每页大小，默认20，最大50", required = false) Integer pageSize) {
        return run(() -> openApiClient.post("/open/token/list", pageBody(current, pageSize, null)));
    }

    @Tool(description = "POST /open/token/add - 新增消息token。"
            + "请求: name(必填), expireTime(可选,默认2999-12-31)。"
            + OpenApiDocs.RESULT + "data: 新建的消息token字符串。")
    public String openTokenAdd(
            @ToolParam(description = "令牌名称，必填") String name,
            @ToolParam(description = "过期时间，如2035-05-09 22:34:00；默认2999-12-31", required = false) String expireTime) {
        JSONObject body = new JSONObject();
        body.set("name", name);
        body.set("expireTime", expireTime);
        return run(() -> openApiClient.post("/open/token/add", body));
    }

    @Tool(description = "DELETE /open/token/deleteToken - 高风险：删除消息token。"
            + OpenApiDocs.RESULT + "data: 如删除成功。")
    public String openTokenDelete(@ToolParam(description = "消息token编号") Long id) {
        return run(() -> openApiClient.delete("/open/token/deleteToken", query("id", id)));
    }

    @Tool(description = "POST /open/token/edit - 修改消息token。"
            + "请求: id(必填), name(必填), expireTime(可选)。"
            + OpenApiDocs.RESULT + "data: 如修改成功。")
    public String openTokenEdit(
            @ToolParam(description = "消息token编号") Long id,
            @ToolParam(description = "令牌名称") String name,
            @ToolParam(description = "过期时间；默认2999-12-31", required = false) String expireTime) {
        JSONObject body = new JSONObject();
        body.set("id", id);
        body.set("name", name);
        body.set("expireTime", expireTime);
        return run(() -> openApiClient.post("/open/token/edit", body));
    }
}
