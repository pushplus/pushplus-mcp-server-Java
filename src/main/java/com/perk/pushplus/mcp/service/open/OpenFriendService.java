package com.perk.pushplus.mcp.service.open;

import cn.hutool.json.JSONObject;
import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class OpenFriendService extends OpenToolSupport {

    public OpenFriendService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "GET /open/friend/getQrCode - 获取个人二维码。"
            + "appId可选; content自定义回调参数; second默认604800最长30天; scanCount:1-999或-1无限。"
            + OpenApiDocs.RESULT + "data: qrCodeImgUrl。")
    public String openFriendGetQrCode(
            @ToolParam(description = "微信公众号Id", required = false) String appId,
            @ToolParam(description = "自定义参数，扫描后回调", required = false) String content,
            @ToolParam(description = "有效期秒；默认604800，最长30天", required = false) Integer second,
            @ToolParam(description = "可扫码次数；1-999，-1无限", required = false) Integer scanCount) {
        return run(() -> openApiClient.get("/open/friend/getQrCode",
                query("appId", appId, "content", content, "second", second, "scanCount", scanCount)));
    }

    @Tool(description = "POST /open/friend/list - 好友列表。"
            + OpenApiDocs.PAGE_REQ + OpenApiDocs.RESULT + OpenApiDocs.PAGE_RESP
            + "list项: id(好友编号),friendId,token(好友令牌),headImgUrl,nickName,"
            + "emailStatus,havePhone,isFollow,remark,createTime。")
    public String openFriendList(
            @ToolParam(description = "当前所在分页数，默认1", required = false) Integer current,
            @ToolParam(description = "每页大小，默认20，最大50", required = false) Integer pageSize) {
        return run(() -> openApiClient.post("/open/friend/list", pageBody(current, pageSize, null)));
    }

    @Tool(description = "GET /open/friend/deleteFriend - 高风险：删除好友。url参数friendId为好友列表friendId字段。"
            + OpenApiDocs.RESULT)
    public String openFriendDelete(
            @ToolParam(description = "好友id（列表friendId字段）") Long friendId,
            @ToolParam(description = "微信公众号Id", required = false) String appId) {
        return run(() -> openApiClient.get("/open/friend/deleteFriend",
                query("friendId", friendId, "appId", appId)));
    }

    @Tool(description = "POST /open/friend/editRemark - 修改好友备注。id=列表id字段，remark必填。"
            + OpenApiDocs.RESULT)
    public String openFriendEditRemark(
            @ToolParam(description = "好友编号（列表id字段）") Long id,
            @ToolParam(description = "好友备注") String remark) {
        JSONObject body = new JSONObject();
        body.set("id", id);
        body.set("remark", remark);
        return run(() -> openApiClient.post("/open/friend/editRemark", body));
    }

    @Tool(description = "POST /open/friend/addBlacklist - 高风险：将好友加入黑名单。"
            + "加入后解除双方好友关系，对方无法再添加你。不能将自己加入黑名单，仅可将已有好友加入。"
            + "url参数friendId为好友列表friendId字段。" + OpenApiDocs.RESULT)
    public String openFriendAddBlacklist(
            @ToolParam(description = "好友id（列表friendId字段）") Long friendId) {
        return run(() -> openApiClient.post("/open/friend/addBlacklist",
                query("friendId", friendId), null));
    }

    @Tool(description = "POST /open/friend/blacklistList - 好友黑名单列表。"
            + OpenApiDocs.PAGE_REQ + OpenApiDocs.RESULT + OpenApiDocs.PAGE_RESP
            + "list项: id(黑名单记录ID,解除时使用),friendId,nickName,headImgUrl,createTime。")
    public String openFriendBlacklistList(
            @ToolParam(description = "当前所在分页数，默认1", required = false) Integer current,
            @ToolParam(description = "每页大小，默认20，最大50", required = false) Integer pageSize) {
        return run(() -> openApiClient.post("/open/friend/blacklistList", pageBody(current, pageSize, null)));
    }

    @Tool(description = "POST /open/friend/removeBlacklist - 高风险：解除好友黑名单。"
            + "解除后不会自动恢复好友关系，需重新扫码添加。url参数id为黑名单列表id字段。"
            + OpenApiDocs.RESULT)
    public String openFriendRemoveBlacklist(
            @ToolParam(description = "黑名单记录ID（列表id字段）") Long id) {
        return run(() -> openApiClient.post("/open/friend/removeBlacklist",
                query("id", id), null));
    }
}
