package com.perk.pushplus.mcp.service.open;

import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class OpenClawBotService extends OpenToolSupport {

    public OpenClawBotService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "GET /open/clawBot/getBotQrcode - 获取ClawBot绑定二维码。无参数。"
            + OpenApiDocs.RESULT + "data: url(二维码地址), qrcode(二维码编号)。")
    public String openClawbotGetBotQrcode() {
        return run(() -> openApiClient.get("/open/clawBot/getBotQrcode", null));
    }

    @Tool(description = "GET /open/clawBot/getQrcodeStatus - 扫码结果查询。url参数qrcode=getBotQrcode返回的qrcode。"
            + OpenApiDocs.RESULT)
    public String openClawbotGetQrcodeStatus(@ToolParam(description = "二维码编号") String qrcode) {
        return run(() -> openApiClient.get("/open/clawBot/getQrcodeStatus", query("qrcode", qrcode), 30000));
    }

    @Tool(description = "GET /open/clawBot/botInfo - 绑定详情。无参数。"
            + OpenApiDocs.RESULT + "data: createTime(绑定时间), haveContextToken(是否有对话令牌)。")
    public String openClawbotBotInfo() {
        return run(() -> openApiClient.get("/open/clawBot/botInfo", null));
    }

    @Tool(description = "GET /open/clawBot/unbind - 高风险：解绑ClawBot。无参数。" + OpenApiDocs.RESULT)
    public String openClawbotUnbind() {
        return run(() -> openApiClient.get("/open/clawBot/unbind", null));
    }

    @Tool(description = "GET /open/clawBot/getMsg - 获取发送消息(可能耗时)。无参数。"
            + OpenApiDocs.RESULT + "data数组项: type(1文字/3语音), text(消息内容)。")
    public String openClawbotGetMsg() {
        return run(() -> openApiClient.get("/open/clawBot/getMsg", null, 60000));
    }
}
