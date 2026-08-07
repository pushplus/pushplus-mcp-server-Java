package com.perk.pushplus.mcp.service.open;

import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class OpenAuthService extends OpenToolSupport {

    public OpenAuthService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "POST /common/openApi/getAccessKey - 获取开放接口AccessKey。"
            + "使用PUSHPLUS_TOKEN(用户token,不支持消息token)+PUSHPLUS_SECRET_KEY。"
            + "有效期约7200秒，重复获取会使上次失效；其他open工具会自动换取缓存。"
            + "需官网开启开放接口并配置安全IP。"
            + OpenApiDocs.RESULT
            + "data: accessKey(后续放header access-key), expiresIn(过期秒数)。")
    public String openGetAccessKey() {
        return run(() -> openApiClient.accessKeyInfoJson(true));
    }
}
