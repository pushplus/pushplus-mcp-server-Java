package com.perk.pushplus.mcp.service.open;

import cn.hutool.json.JSONObject;
import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class OpenQqBotService extends OpenToolSupport {

    public OpenQqBotService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "GET /open/qqBot/getBindLink - 获取QQ机器人绑定链接与绑定码。"
            + "url参数refresh(可选,默认false;true会使旧绑定码失效并重新生成)。" + OpenApiDocs.RESULT
            + "data: url(带参分享链接,可生成二维码,已绑定用户可能为空), bindCode(绑定码,已是好友时需私聊发给机器人,认领QQ群也用此码), "
            + "expireSeconds(有效期秒数,默认300), botAppId, botName, botAvatar。")
    public String openQqBotGetBindLink(
            @ToolParam(description = "是否强制刷新绑定码，默认false", required = false) Boolean refresh) {
        return run(() -> openApiClient.get("/open/qqBot/getBindLink", query("refresh", refresh)));
    }

    @Tool(description = "GET /open/qqBot/botInfo - 查询QQ机器人绑定状态。无参数。" + OpenApiDocs.RESULT
            + "data: isBind(0未绑定/1已绑定), receiveStatus(1可接收/0用户已关闭单聊接收), createTime, "
            + "botInfo(botId,username,avatar,appId,shareUrl(可用于拉机器人进群))。")
    public String openQqBotInfo() {
        return run(() -> openApiClient.get("/open/qqBot/botInfo", null));
    }

    @Tool(description = "GET /open/qqBot/unbind - 高风险：解绑QQ机器人。无参数。" + OpenApiDocs.RESULT)
    public String openQqBotUnbind() {
        return run(() -> openApiClient.get("/open/qqBot/unbind", null));
    }

    @Tool(description = "GET /open/qqBot/groupList - 获取机器人已加入的QQ群列表。无参数。" + OpenApiDocs.RESULT
            + "data数组项: id(QQ群编号,新增群配置时作为qqGroupId), groupOpenId, groupRemark, "
            + "status(1在群/2群消息接收关闭), groupName(接口未授权时为空), groupFingerMemo, groupClassText, "
            + "groupTags, groupMemberNum, createTime。")
    public String openQqBotGroupList() {
        return run(() -> openApiClient.get("/open/qqBot/groupList", null));
    }

    @Tool(description = "POST /open/qqBot/list - QQ机器人群配置列表。"
            + OpenApiDocs.PAGE_REQ + OpenApiDocs.RESULT + OpenApiDocs.PAGE_RESP
            + "list项: id(配置编号), qqName, qqCode(配置编码,发送消息时作为option传入), sendType(2发到QQ群), "
            + "qqGroupId, groupRemark, groupOpenId, groupName, updateTime。")
    public String openQqBotList(
            @ToolParam(description = "当前所在分页数，默认1", required = false) Integer current,
            @ToolParam(description = "每页大小，默认20，最大50", required = false) Integer pageSize) {
        return run(() -> openApiClient.post("/open/qqBot/list", pageBody(current, pageSize, null)));
    }

    @Tool(description = "POST /open/qqBot/add - 新增QQ机器人群配置（用于发到指定QQ群，发给自己无需配置）。"
            + "必填: qqName(最多64字符), qqCode(最多32字符,仅字母/数字/下划线/中划线,创建后不可修改), "
            + "qqGroupId(取自groupList的id,该群需允许机器人主动消息)。"
            + "限制: 普通用户最多5个,会员最多30个,同一QQ群不可重复创建。" + OpenApiDocs.RESULT)
    public String openQqBotAdd(
            @ToolParam(description = "配置名称，最多64个字符") String qqName,
            @ToolParam(description = "配置编码，最多32个字符，仅支持字母、数字、下划线和中划线") String qqCode,
            @ToolParam(description = "QQ群编号，取自groupList接口的id") Long qqGroupId) {
        JSONObject json = new JSONObject();
        json.set("qqName", qqName);
        json.set("qqCode", qqCode);
        json.set("qqGroupId", qqGroupId);
        return run(() -> openApiClient.post("/open/qqBot/add", json));
    }

    @Tool(description = "POST /open/qqBot/edit - 修改QQ机器人群配置。配置编码(qqCode)不允许修改，避免已在使用的option失效。"
            + "必填: id,qqName,qqGroupId。" + OpenApiDocs.RESULT)
    public String openQqBotEdit(
            @ToolParam(description = "配置编号") Long id,
            @ToolParam(description = "配置名称，最多64个字符") String qqName,
            @ToolParam(description = "QQ群编号") Long qqGroupId) {
        JSONObject json = new JSONObject();
        json.set("id", id);
        json.set("qqName", qqName);
        json.set("qqGroupId", qqGroupId);
        return run(() -> openApiClient.post("/open/qqBot/edit", json));
    }

    @Tool(description = "DELETE /open/qqBot/delete - 高风险：删除QQ机器人群配置，删除后使用该编码的option将失效。"
            + "url参数id=配置编号。" + OpenApiDocs.RESULT)
    public String openQqBotDelete(@ToolParam(description = "配置编号") Long id) {
        return run(() -> openApiClient.delete("/open/qqBot/delete", query("id", id)));
    }
}
