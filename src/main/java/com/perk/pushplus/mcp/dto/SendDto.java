package com.perk.pushplus.mcp.dto;

import lombok.Data;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.Serializable;

@Data
public class SendDto implements Serializable {

    @ToolParam(description = "用户token或消息token", required = false)
    private String token;

    @ToolParam(description = "消息标题",required = false)
    private String title;

    @ToolParam(description = "具体消息内容，根据不同template支持不同格式")
    private String content;

    @ToolParam(description = "群组编码，不填仅发送给自己；channel为webhook时无效", required = false)
    private String topic;

    @ToolParam(description = "发送模板,默认值：html;发送模板枚举：html(默认模板，支持html文本),txt(纯文本展示，不转义html),json(内容基于json格式展示),markdown(内容基于markdown格式展示),cloudMonitor(阿里云监控报警定制模板),jenkins(jenkins插件定制模板),route(路由器插件定制模板),pay(支付成功通知模板)", required = false)
    private String template;

    @ToolParam(description = "发送渠道,默认值：wechat;发送渠道枚举：wechat(微信公众号),webhook(第三方webhook；企业微信、钉钉、飞书、server酱、IFTTT),cp(企业微信应用),mail(邮箱),sms(短信)",required = false)
    private String channel;

    @ToolParam(description = "webhook编码", required = false)
    private String webhook;

    @ToolParam(description = "发送结果回调地址", required = false)
    private String callbackUrl;

    @ToolParam(description = "毫秒时间戳。格式如：1632993318000。服务器时间戳大于此时间戳，则消息不会发送", required = false)
    private Long timestamp;

    @ToolParam(description = "好友令牌，微信公众号渠道填写好友令牌，企业微信渠道填写企业微信用户id。多人用逗号隔开", required = false)
    private String to;

    @ToolParam(description = "预处理编码", required = false)
    private String pre;

}
