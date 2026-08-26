package com.perk.pushplus.mcp.dto;

import lombok.Data;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.Serializable;

@Data
public class SendDto implements Serializable {

    @ToolParam(description = "用户token或消息token", required = false)
    private String token;

    @ToolParam(description = "消息标题，可选；非会员最多100字符，会员最多200字符，不可含换行", required = false)
    private String title;

    @ToolParam(description = "具体消息内容，必填；根据不同template支持不同格式")
    private String content;

    @ToolParam(description = "消息图标，可选", required = false)
    private String icon;

    @ToolParam(description = "群组编码，不填仅发送给自己；channel为webhook时无效；与to互斥，topic优先", required = false)
    private String topic;

    @ToolParam(description = "发送模板，默认html。枚举：html(默认HTML),txt(纯文本),json(JSON展示),markdown(Markdown),cloudMonitor(阿里云监控),jenkins(Jenkins插件),route(路由器插件),pay(支付成功),order(订单支付成功),verify(实名认证),form(表单，需pushId),doc(文档，需pushId),excel(表格，需pushId),webdiff(网页差异对比，需pushId)", required = false)
    private String template;

    @ToolParam(description = "发送渠道，默认wechat。枚举：wechat(微信公众号),webhook(第三方webhook),cp(企业微信应用),mail(邮箱),sms(短信),voice(语音),extension(插件),app(App),clawbot(微信ClawBot),qq(QQ机器人)。batchSend时多个渠道用逗号隔开，最多5个", required = false)
    private String channel;

    @ToolParam(description = "webhook编码（非URL），channel为webhook/cp/mail等时使用", required = false)
    private String webhook;

    @ToolParam(description = "渠道配置参数(原webhook参数)，与webhook等价；qq渠道不填则发给自己，填群配置编码则发到对应QQ群；batchSend时多个用逗号隔开，与channel一一对应。如：\",config1,\"", required = false)
    private String option;

    @ToolParam(description = "发送结果回调地址", required = false)
    private String callbackUrl;

    @ToolParam(description = "毫秒时间戳字符串，如：1632993318000；服务器时间戳大于此值则消息不会发送", required = false)
    private String timestamp;

    @ToolParam(description = "好友令牌；微信公众号渠道、QQ机器人渠道填好友令牌，企业微信渠道填企业微信用户id；多人用逗号隔开；与topic互斥", required = false)
    private String to;

    @ToolParam(description = "预处理编码，会员可用", required = false)
    private String pre;

    @ToolParam(description = "push类模板详情页ID；template为form/doc/excel/webdiff时必填", required = false)
    private String pushId;

}
