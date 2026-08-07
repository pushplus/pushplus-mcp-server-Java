package com.perk.pushplus.mcp;

import com.perk.pushplus.mcp.service.PushplusService;
import com.perk.pushplus.mcp.service.open.OpenAuthService;
import com.perk.pushplus.mcp.service.open.OpenChannelService;
import com.perk.pushplus.mcp.service.open.OpenClawBotService;
import com.perk.pushplus.mcp.service.open.OpenFileService;
import com.perk.pushplus.mcp.service.open.OpenFriendService;
import com.perk.pushplus.mcp.service.open.OpenMessageService;
import com.perk.pushplus.mcp.service.open.OpenPayService;
import com.perk.pushplus.mcp.service.open.OpenPreService;
import com.perk.pushplus.mcp.service.open.OpenSettingService;
import com.perk.pushplus.mcp.service.open.OpenTokenService;
import com.perk.pushplus.mcp.service.open.OpenTopicService;
import com.perk.pushplus.mcp.service.open.OpenTopicUserService;
import com.perk.pushplus.mcp.service.open.OpenUserService;
import com.perk.pushplus.mcp.service.open.OpenWebhookService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@ConfigurationPropertiesScan(basePackages = "com.perk.pushplus.mcp.properties")
@SpringBootApplication
public class PushplusMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(PushplusMcpApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider serverTools(
            PushplusService pushplusService,
            OpenAuthService openAuthService,
            OpenUserService openUserService,
            OpenMessageService openMessageService,
            OpenTokenService openTokenService,
            OpenTopicService openTopicService,
            OpenTopicUserService openTopicUserService,
            OpenFriendService openFriendService,
            OpenWebhookService openWebhookService,
            OpenSettingService openSettingService,
            OpenPreService openPreService,
            OpenChannelService openChannelService,
            OpenClawBotService openClawBotService,
            OpenFileService openFileService,
            OpenPayService openPayService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(
                        pushplusService,
                        openAuthService,
                        openUserService,
                        openMessageService,
                        openTokenService,
                        openTopicService,
                        openTopicUserService,
                        openFriendService,
                        openWebhookService,
                        openSettingService,
                        openPreService,
                        openChannelService,
                        openClawBotService,
                        openFileService,
                        openPayService
                )
                .build();
    }
}
