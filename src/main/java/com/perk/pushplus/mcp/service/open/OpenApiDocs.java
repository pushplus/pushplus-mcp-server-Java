package com.perk.pushplus.mcp.service.open;

/**
 * 开放接口文档字段说明常量（对齐 https://www.pushplus.plus/doc/guide/openApi.html V1.15）
 */
final class OpenApiDocs {

    static final String RESULT =
            "统一响应: code(200成功), msg, data。";
    static final String PAGE_REQ =
            "分页: current(默认1), pageSize(默认20,最大50)。";
    static final String PAGE_RESP =
            "分页data: pageNum, pageSize, total, pages, list。";
    static final String WEBHOOK_TYPE =
            "webhookType:1企业微信机器人,2钉钉,3飞书,4Server酱,50bark,6企业微信应用,7腾讯轻联,8IFTTT,9集简云,10Gotify,11WxPusher,12自定义";
    static final String CHANNEL =
            "channel: wechat/cp/webhook/mail/sms/voice/extension";
    static final String TOPIC_TYPE =
            "topicType:0普通群组;1积分群组;2公开群组";

    private OpenApiDocs() {
    }
}
