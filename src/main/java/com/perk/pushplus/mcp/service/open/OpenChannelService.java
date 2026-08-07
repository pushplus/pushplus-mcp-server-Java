package com.perk.pushplus.mcp.service.open;

import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class OpenChannelService extends OpenToolSupport {

    public OpenChannelService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "POST /open/mail/list - 邮箱渠道列表。"
            + OpenApiDocs.PAGE_REQ + OpenApiDocs.RESULT + OpenApiDocs.PAGE_RESP
            + "list项: id,mailName,mailCode。")
    public String openMailList(
            @ToolParam(description = "当前所在分页数，默认1", required = false) Integer current,
            @ToolParam(description = "每页大小，默认20，最大50", required = false) Integer pageSize) {
        return run(() -> openApiClient.post("/open/mail/list", pageBody(current, pageSize, null)));
    }

    @Tool(description = "GET /open/mail/detail - 邮箱渠道详情。"
            + OpenApiDocs.RESULT
            + "data: id,mailName,mailCode,account,password,smtpServer,smtpSsl(1/0),smtpPort,createTime。")
    public String openMailDetail(@ToolParam(description = "邮箱编号") Long mailId) {
        return run(() -> openApiClient.get("/open/mail/detail", query("mailId", mailId)));
    }

    @Tool(description = "POST /open/mp/list - 微信公众号渠道列表。"
            + OpenApiDocs.PAGE_REQ + OpenApiDocs.RESULT + OpenApiDocs.PAGE_RESP
            + "list项: id,nickName,headImg,principalName,authorizationAppid,funcInfo,"
            + "serviceType(0订阅号/1历史升级订阅号/2服务号),verifyType(-1未认证/0微信认证),alias,updateTime。")
    public String openMpList(
            @ToolParam(description = "当前所在分页数，默认1", required = false) Integer current,
            @ToolParam(description = "每页大小，默认20，最大50", required = false) Integer pageSize) {
        return run(() -> openApiClient.post("/open/mp/list", pageBody(current, pageSize, null)));
    }

    @Tool(description = "GET /open/mp/detail - 公众号详情。请求id=微信公众号编号。" + OpenApiDocs.RESULT)
    public String openMpDetail(@ToolParam(description = "微信公众号编号") Long id) {
        return run(() -> openApiClient.get("/open/mp/detail", query("id", id)));
    }

    @Tool(description = "POST /open/cp/list - 企业微信应用渠道列表。"
            + OpenApiDocs.PAGE_REQ + OpenApiDocs.RESULT + OpenApiDocs.PAGE_RESP
            + "list项: id,cpName,cpCode。")
    public String openCpList(
            @ToolParam(description = "当前所在分页数，默认1", required = false) Integer current,
            @ToolParam(description = "每页大小，默认20，最大50", required = false) Integer pageSize) {
        return run(() -> openApiClient.post("/open/cp/list", pageBody(current, pageSize, null)));
    }
}
