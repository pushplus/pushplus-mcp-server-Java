package com.perk.pushplus.mcp.service.open;

import cn.hutool.json.JSONObject;
import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class OpenTopicService extends OpenToolSupport {

    public OpenTopicService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "POST /open/topic/list - 群组列表。params.topicType:0我创建的/1我加入的。"
            + OpenApiDocs.PAGE_REQ + OpenApiDocs.RESULT + OpenApiDocs.PAGE_RESP
            + "list项: icon,topicId,topicCode,topicName,nickName,createTime,topicUserCount,"
            + OpenApiDocs.TOPIC_TYPE + ",isApproved(0未审/1不通过/2通过),firstIsApproved,approveReason,isOpen(0否/1是)。")
    public String openTopicList(
            @ToolParam(description = "当前所在分页数，默认1", required = false) Integer current,
            @ToolParam(description = "每页大小，默认20，最大50", required = false) Integer pageSize,
            @ToolParam(description = "0-我创建的，1-我加入的；默认0", required = false) Integer topicType) {
        JSONObject params = new JSONObject();
        params.set("topicType", topicType == null ? 0 : topicType);
        return run(() -> openApiClient.post("/open/topic/list", pageBody(current, pageSize, params)));
    }

    @Tool(description = "GET /open/topic/detail - 我创建的群组详情。"
            + OpenApiDocs.RESULT
            + "data: topicId/Code/Name,qrCodeImgUrl,contact,introduction,receiptMessage,nickName,createTime,"
            + "topicUserCount,icon,appId," + OpenApiDocs.TOPIC_TYPE
            + ",price(积分/月),topicDescribe,userNickName,isApproved,firstIsApproved,approveReason,isOpen。")
    public String openTopicDetail(@ToolParam(description = "群组编号") Long topicId) {
        return run(() -> openApiClient.get("/open/topic/detail", query("topicId", topicId)));
    }

    @Tool(description = "GET /open/topic/joinTopicDetail - 我加入的群详情。"
            + OpenApiDocs.RESULT
            + "data: topicId/Code/Name,contact,introduction,nickName,createTime(加入时间),icon,topicUserCount,"
            + OpenApiDocs.TOPIC_TYPE + ",price,topicDescribe,userNickName。")
    public String openTopicJoinDetail(@ToolParam(description = "群组编号") Long topicId) {
        return run(() -> openApiClient.get("/open/topic/joinTopicDetail", query("topicId", topicId)));
    }

    @Tool(description = "POST /open/topic/add - 新增群组。"
            + "必填: topicCode,topicName,contact,introduction；可选: receiptMessage,appId,icon,topicType,price,topicDescribe。"
            + OpenApiDocs.RESULT + "data: 新建群组编号。")
    public String openTopicAdd(
            @ToolParam(description = "群组编码，必填") String topicCode,
            @ToolParam(description = "群组名称，必填") String topicName,
            @ToolParam(description = "联系方式，必填") String contact,
            @ToolParam(description = "群组简介，必填") String introduction,
            @ToolParam(description = "加入后回复内容", required = false) String receiptMessage,
            @ToolParam(description = "微信公众号Id；默认pushplus公众号", required = false) String appId,
            @ToolParam(description = "群组图标", required = false) String icon,
            @ToolParam(description = "0普通/1积分/2公开；默认0", required = false) Integer topicType,
            @ToolParam(description = "积分群组订阅积分(按月)", required = false) Long price,
            @ToolParam(description = "一句话介绍", required = false) String topicDescribe) {
        JSONObject body = new JSONObject();
        body.set("topicCode", topicCode);
        body.set("topicName", topicName);
        body.set("contact", contact);
        body.set("introduction", introduction);
        body.set("receiptMessage", receiptMessage);
        body.set("appId", appId);
        body.set("icon", icon);
        body.set("topicType", topicType);
        body.set("price", price);
        body.set("topicDescribe", topicDescribe);
        return run(() -> openApiClient.post("/open/topic/add", body));
    }

    @Tool(description = "GET /open/topic/qrCode - 获取群组二维码。"
            + "second默认604800(7天)最长30天；scanCount:1-999或-1无限。"
            + OpenApiDocs.RESULT + "data: qrCodeImgUrl, forever(0临时/1永久)。")
    public String openTopicQrCode(
            @ToolParam(description = "群组编号") Long topicId,
            @ToolParam(description = "有效期秒；默认604800，最长30天", required = false) Integer second,
            @ToolParam(description = "可扫码次数；1-999，-1无限", required = false) Integer scanCount) {
        return run(() -> openApiClient.get("/open/topic/qrCode",
                query("topicId", topicId, "second", second, "scanCount", scanCount)));
    }

    @Tool(description = "GET /open/topic/exitTopic - 高风险：退出群组。" + OpenApiDocs.RESULT + "data: 如退订成功。")
    public String openTopicExit(@ToolParam(description = "群组编号") Long topicId) {
        return run(() -> openApiClient.get("/open/topic/exitTopic", query("topicId", topicId)));
    }

    @Tool(description = "GET /open/topic/delete - 高风险：删除群组。" + OpenApiDocs.RESULT + "data: 如群组删除成功。")
    public String openTopicDelete(@ToolParam(description = "群组编号") Long topicId) {
        return run(() -> openApiClient.get("/open/topic/delete", query("topicId", topicId)));
    }

    @Tool(description = "POST /open/topic/editTopic - 修改群组。"
            + "必填: topicId,topicCode,topicName；可选: contact,introduction,receiptMessage,icon,price,topicDescribe。"
            + OpenApiDocs.RESULT + "data: 如修改成功。")
    public String openTopicEdit(
            @ToolParam(description = "群组编号") Long topicId,
            @ToolParam(description = "群组编码") String topicCode,
            @ToolParam(description = "群组名称") String topicName,
            @ToolParam(description = "联系方式", required = false) String contact,
            @ToolParam(description = "群组简介", required = false) String introduction,
            @ToolParam(description = "加入后回复内容", required = false) String receiptMessage,
            @ToolParam(description = "群组图标", required = false) String icon,
            @ToolParam(description = "积分群组订阅积分(按月)", required = false) Long price,
            @ToolParam(description = "一句话介绍", required = false) String topicDescribe) {
        JSONObject body = new JSONObject();
        body.set("topicId", topicId);
        body.set("topicCode", topicCode);
        body.set("topicName", topicName);
        body.set("contact", contact);
        body.set("introduction", introduction);
        body.set("receiptMessage", receiptMessage);
        body.set("icon", icon);
        body.set("price", price);
        body.set("topicDescribe", topicDescribe);
        return run(() -> openApiClient.post("/open/topic/editTopic", body));
    }

    @Tool(description = "POST /open/topic/isOpen - 积分群组上下架。请求: topicId,isOpen(1上架/0下架)。"
            + OpenApiDocs.RESULT + "data: 如操作成功。")
    public String openTopicIsOpen(
            @ToolParam(description = "群组编号") Long topicId,
            @ToolParam(description = "是否上架；1是，0否") Integer isOpen) {
        JSONObject body = new JSONObject();
        body.set("topicId", topicId);
        body.set("isOpen", isOpen);
        return run(() -> openApiClient.post("/open/topic/isOpen", body));
    }
}
