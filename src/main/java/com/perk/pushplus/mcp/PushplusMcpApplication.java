package com.perk.pushplus.mcp;

import com.perk.pushplus.mcp.service.PushplusService;
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
    public ToolCallbackProvider serverTools(PushplusService pushplusService) {
        MethodToolCallbackProvider methodToolCallbackProvider =
                MethodToolCallbackProvider
                        .builder()
                        .toolObjects(pushplusService)
                        .build();

        return methodToolCallbackProvider;
    }

}
