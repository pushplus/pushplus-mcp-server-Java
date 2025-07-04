package com.perk.pushplus.mcp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "pushplus")
public class PushplusProperties {

    private String url;

    private String token;

}
