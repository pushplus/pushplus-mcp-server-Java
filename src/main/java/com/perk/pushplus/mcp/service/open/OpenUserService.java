package com.perk.pushplus.mcp.service.open;

import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class OpenUserService extends OpenToolSupport {

    public OpenUserService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "GET /open/user/token - 获取当前用户token。无请求参数。"
            + OpenApiDocs.RESULT + "data: 用户token字符串。")
    public String openUserToken() {
        return run(() -> openApiClient.get("/open/user/token", null));
    }

    @Tool(description = "GET /open/user/myInfo - 个人资料详情。无请求参数。"
            + OpenApiDocs.RESULT
            + "data: openId,unionId,nickName,headImgUrl,userSex(0未设置/1男/2女),token,phoneNumber,email,"
            + "emailStatus(0未验证/1待验证/2已验证),birthday,points,verifyStatus(0未实名/1已实名),"
            + "vipInfo{isVip(0/1),lastDay}。")
    public String openUserMyInfo() {
        return run(() -> openApiClient.get("/open/user/myInfo", null));
    }

    @Tool(description = "GET /open/user/userLimitTime - 获取解封剩余时间。无请求参数。"
            + OpenApiDocs.RESULT
            + "data: sendLimit(1无限制/2短期限制/3永久限制), userLimitTime(解封时间)。")
    public String openUserLimitTime() {
        return run(() -> openApiClient.get("/open/user/userLimitTime", null));
    }

    @Tool(description = "GET /open/user/sendCount - 查询当日消息接口请求次数。无请求参数。"
            + OpenApiDocs.RESULT
            + "data: wechatSendCount,cpSendCount,webhookSendCount,mailSendCount。")
    public String openUserSendCount() {
        return run(() -> openApiClient.get("/open/user/sendCount", null));
    }
}
