package com.perk.pushplus.mcp.service.open;

import cn.hutool.json.JSONObject;
import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class OpenSettingService extends OpenToolSupport {

    public OpenSettingService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "GET /open/setting/getUserSettings - 已废弃，请用listUserDefault。" + OpenApiDocs.RESULT)
    public String openSettingGetUserSettings() {
        return run(() -> openApiClient.get("/open/setting/getUserSettings", null));
    }

    @Tool(description = "POST /open/setting/changeDefaultChannel - 已废弃，请用add/editUserDefault。" + OpenApiDocs.RESULT)
    public String openSettingChangeDefaultChannel(
            @ToolParam(description = "默认渠道编码") String defaultChannel,
            @ToolParam(description = "默认webhook/渠道参数", required = false) String defaultWebhook) {
        JSONObject body = new JSONObject();
        body.set("defaultChannel", defaultChannel);
        body.set("defaultWebhook", defaultWebhook);
        return run(() -> openApiClient.post("/open/setting/changeDefaultChannel", body));
    }

    @Tool(description = "POST /open/setting/listUserDefault - 默认配置列表。"
            + OpenApiDocs.PAGE_REQ + OpenApiDocs.RESULT + OpenApiDocs.PAGE_RESP
            + "list项: id,channel(" + OpenApiDocs.CHANNEL + "),channelTxt,updateTime,name(令牌名称)。")
    public String openSettingListUserDefault(
            @ToolParam(description = "当前所在分页数，默认1", required = false) Integer current,
            @ToolParam(description = "每页大小，默认20，最大50", required = false) Integer pageSize) {
        return run(() -> openApiClient.post("/open/setting/listUserDefault", pageBody(current, pageSize, null)));
    }

    @Tool(description = "GET /open/setting/detailUserDefault - 默认配置详情。"
            + OpenApiDocs.RESULT
            + "data: id,channel,option(渠道参数),pre,updateTime,name,tokenId(消息令牌id;用户令牌为0)。")
    public String openSettingDetailUserDefault(@ToolParam(description = "默认配置编号") Long id) {
        return run(() -> openApiClient.get("/open/setting/detailUserDefault", query("id", id)));
    }

    @Tool(description = "POST /open/setting/addUserDefault - 新增默认配置。"
            + "必填: channel,option,pre,tokenId(用户令牌填0)。" + OpenApiDocs.CHANNEL + "。" + OpenApiDocs.RESULT)
    public String openSettingAddUserDefault(
            @ToolParam(description = OpenApiDocs.CHANNEL) String channel,
            @ToolParam(description = "渠道参数；webhook/cp需填具体编码") String option,
            @ToolParam(description = "预处理编码；无则空串") String pre,
            @ToolParam(description = "消息令牌id；用户令牌为0") Long tokenId) {
        JSONObject body = new JSONObject();
        body.set("channel", channel);
        body.set("option", option);
        body.set("pre", pre);
        body.set("tokenId", tokenId);
        return run(() -> openApiClient.post("/open/setting/addUserDefault", body));
    }

    @Tool(description = "POST /open/setting/editUserDefault - 修改默认配置。"
            + "必填: id,channel,tokenId；可选option/pre。" + OpenApiDocs.RESULT + "data: 如修改成功。")
    public String openSettingEditUserDefault(
            @ToolParam(description = "默认配置编号") Long id,
            @ToolParam(description = OpenApiDocs.CHANNEL) String channel,
            @ToolParam(description = "渠道参数", required = false) String option,
            @ToolParam(description = "预处理编码", required = false) String pre,
            @ToolParam(description = "消息令牌id；用户令牌为0") Long tokenId) {
        JSONObject body = new JSONObject();
        body.set("id", id);
        body.set("channel", channel);
        body.set("option", option);
        body.set("pre", pre);
        body.set("tokenId", tokenId);
        return run(() -> openApiClient.post("/open/setting/editUserDefault", body));
    }

    @Tool(description = "DELETE /open/setting/deleteUserDefault - 高风险：删除默认配置。"
            + OpenApiDocs.RESULT + "data: 如默认配置删除成功。")
    public String openSettingDeleteUserDefault(@ToolParam(description = "默认配置编号") Long id) {
        return run(() -> openApiClient.delete("/open/setting/deleteUserDefault", query("id", id)));
    }

    @Tool(description = "GET /open/setting/changeRecevieLimit - 修改接收消息限制。recevieLimit:0接收全部/1不接收。"
            + OpenApiDocs.RESULT)
    public String openSettingChangeReceiveLimit(
            @ToolParam(description = "0-接收全部，1-不接收消息") Integer recevieLimit) {
        return run(() -> openApiClient.get("/open/setting/changeRecevieLimit", query("recevieLimit", recevieLimit)));
    }

    @Tool(description = "GET /open/setting/changeIsSend - 开启/关闭发送。isSend:0禁用/1启用。" + OpenApiDocs.RESULT)
    public String openSettingChangeIsSend(@ToolParam(description = "0-禁用，1-启用") Integer isSend) {
        return run(() -> openApiClient.get("/open/setting/changeIsSend", query("isSend", isSend)));
    }

    @Tool(description = "GET /open/setting/changeOpenMessageType - 修改打开消息方式。openMessageType:0 H5/1小程序。"
            + OpenApiDocs.RESULT)
    public String openSettingChangeOpenMessageType(
            @ToolParam(description = "0:H5，1:小程序") Integer openMessageType) {
        return run(() -> openApiClient.get("/open/setting/changeOpenMessageType",
                query("openMessageType", openMessageType)));
    }

    @Tool(description = "GET /open/setting/extension - 插件转发。forward:0否/1是(微信消息同步插件与桌面应用)。"
            + OpenApiDocs.RESULT)
    public String openSettingExtension(@ToolParam(description = "0:否，1:是") Integer forward) {
        return run(() -> openApiClient.get("/open/setting/extension", query("forward", forward)));
    }
}
