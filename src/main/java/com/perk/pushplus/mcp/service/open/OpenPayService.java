package com.perk.pushplus.mcp.service.open;

import cn.hutool.json.JSONObject;
import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class OpenPayService extends OpenToolSupport {

    public OpenPayService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "POST /open/pay/transferOrder - 高风险：积分提现。"
            + "请求: accountId(收款账户ID,必填), points(提现积分,可选)。"
            + OpenApiDocs.RESULT + "data: 提现业务结果。")
    public String openPayTransferOrder(
            @ToolParam(description = "收款账户ID") Long accountId,
            @ToolParam(description = "提现积分", required = false) Double points) {
        JSONObject body = new JSONObject();
        body.set("accountId", accountId);
        body.set("points", points);
        return run(() -> openApiClient.post("/open/pay/transferOrder", body));
    }
}
