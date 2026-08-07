package com.perk.pushplus.mcp.service.open;

import cn.hutool.json.JSONObject;
import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class OpenTopicUserService extends OpenToolSupport {

    public OpenTopicUserService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "POST /open/topicUser/subscriberList - 获取群组内用户。params.topicId必填。"
            + OpenApiDocs.PAGE_REQ + OpenApiDocs.RESULT + OpenApiDocs.PAGE_RESP
            + "list项: id(可用于删除),nickName,openId,headImgUrl,userSex(0/1/2),"
            + "havePhone(0/1),isFollow(0/1),emailStatus(0/1/2),followTime,remark。")
    public String openTopicUserSubscriberList(
            @ToolParam(description = "群组编号，必填") Long topicId,
            @ToolParam(description = "当前所在分页数，默认1", required = false) Integer current,
            @ToolParam(description = "每页大小，默认20，最大50", required = false) Integer pageSize) {
        JSONObject params = new JSONObject();
        params.set("topicId", topicId);
        return run(() -> openApiClient.post("/open/topicUser/subscriberList", pageBody(current, pageSize, params)));
    }

    @Tool(description = "POST /open/topicUser/deleteTopicUser - 高风险：删除群组内用户。"
            + "url参数topicRelationId=订阅人列表id。" + OpenApiDocs.RESULT + "data: 如删除成功。")
    public String openTopicUserDelete(
            @ToolParam(description = "用户编号（订阅关系ID），来自订阅人列表id") Long topicRelationId) {
        return run(() -> openApiClient.post("/open/topicUser/deleteTopicUser",
                query("topicRelationId", topicRelationId), null));
    }

    @Tool(description = "POST /open/topicUser/editRemark - 修改订阅人备注。id必填，remark必填且20字以内。"
            + OpenApiDocs.RESULT)
    public String openTopicUserEditRemark(
            @ToolParam(description = "用户编号（订阅关系ID）") Long id,
            @ToolParam(description = "订阅人备注；20个字以内") String remark) {
        JSONObject body = new JSONObject();
        body.set("id", id);
        body.set("remark", remark);
        return run(() -> openApiClient.post("/open/topicUser/editRemark", body));
    }
}
