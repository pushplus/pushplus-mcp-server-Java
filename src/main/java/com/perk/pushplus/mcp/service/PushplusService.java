package com.perk.pushplus.mcp.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.perk.pushplus.mcp.dto.SendDto;
import com.perk.pushplus.mcp.properties.PushplusProperties;
import com.perk.pushplus.mcp.vo.ResultVo;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PushplusService {

    private static final String TEMPLATE_ENUM =
            "html/txt/json/markdown/cloudMonitor/jenkins/route/pay/order/verify/form/doc/excel/webdiff";
    private static final String CHANNEL_ENUM =
            "wechat/webhook/cp/mail/sms/voice/extension/app/clawbot";
    private static final int SEND_TIMEOUT_MS = 10000;
    private static final int BATCH_SEND_TIMEOUT_MS = 15000;

    @Resource
    private PushplusProperties pushplusProperties;

    @Tool(description = "使用pushplus发送消息（对应 /send 接口）。请求成功返回消息流水号(shortCode)，失败返回具体原因。" +
            "form/doc/excel/webdiff 模板必须同时传 pushId。")
    public String send(@ToolParam(description = "发送消息参数，字段：" +
            "token(用户token或消息token，可选，未传则用环境变量PUSHPLUS_TOKEN)，" +
            "title(消息标题，可选)，" +
            "content(消息内容，必填)，" +
            "icon(消息图标，可选)，" +
            "topic(群组编码，可选，与to互斥，topic优先)，" +
            "template(发送模板，可选，默认html，支持：" + TEMPLATE_ENUM + ")，" +
            "channel(发送渠道，可选，默认wechat，支持：" + CHANNEL_ENUM + ")，" +
            "webhook(webhook编码，可选，非URL)，" +
            "option(渠道配置参数，可选，与webhook等价)，" +
            "callbackUrl(发送结果回调地址，可选)，" +
            "timestamp(毫秒时间戳字符串，可选)，" +
            "to(好友令牌/企微用户id，可选，多人逗号分隔)，" +
            "pre(预处理编码，可选)，" +
            "pushId(form/doc/excel/webdiff模板必填)") SendDto sendDto) {
        if (sendDto != null && !StringUtils.hasText(sendDto.getToken())) {
            sendDto.setToken(pushplusProperties.getToken());
        }

        String validateError = validatePushId(sendDto);
        if (validateError != null) {
            return validateError;
        }

        HttpResponse response = HttpRequest.post(pushplusProperties.getUrl())
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(sendDto))
                .timeout(SEND_TIMEOUT_MS)
                .execute();
        String result = response.body();

        ResultVo resultVo = JSONUtil.toBean(result, ResultVo.class);
        if (resultVo != null) {
            String msg = resultVo.getMsg();
            if (resultVo.getCode() == 200) {
                msg += "消息流水号:" + resultVo.getData();
            }
            return msg;
        }
        return "请求失败";
    }

    @Tool(description = "通过pushplus同时向多个渠道发送消息（对应 /batchSend 接口）。" +
            "channel 多个用逗号隔开（最多5个），option 与 channel 一一对应。" +
            "成功返回各渠道流水号，失败返回具体原因。form/doc/excel/webdiff 模板必须同时传 pushId。")
    public String batchSend(@ToolParam(description = "多渠道发送消息参数，字段：" +
            "token(用户token或消息token，可选)，" +
            "title(消息标题，可选)，" +
            "content(消息内容，必填)，" +
            "icon(消息图标，可选)，" +
            "channel(发送渠道，可选，默认wechat，多个用逗号隔开，如：\"wechat,mail,webhook\"，最多5个)，" +
            "option(渠道配置参数，可选，多个用逗号隔开与channel一一对应，如：\",config1,\")，" +
            "topic(群组编码，可选)，" +
            "template(发送模板，可选，默认html，支持：" + TEMPLATE_ENUM + ")，" +
            "callbackUrl(发送结果回调地址，可选)，" +
            "timestamp(毫秒时间戳字符串，可选)，" +
            "to(好友令牌/企微用户id，可选，多人逗号分隔)，" +
            "pre(预处理编码，可选，会员可用)，" +
            "pushId(form/doc/excel/webdiff模板必填)") SendDto sendDto) {
        if (sendDto != null && !StringUtils.hasText(sendDto.getToken())) {
            sendDto.setToken(pushplusProperties.getToken());
        }

        String validateError = validatePushId(sendDto);
        if (validateError != null) {
            return validateError;
        }

        HttpResponse response = HttpRequest.post(pushplusProperties.getBatchUrl())
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(sendDto))
                .timeout(BATCH_SEND_TIMEOUT_MS)
                .execute();
        String result = response.body();

        JSONObject jsonResult = JSONUtil.parseObj(result);
        int code = jsonResult.getInt("code", -1);
        String msg = jsonResult.getStr("msg", "");

        if (code == 200) {
            JSONArray dataArray = jsonResult.getJSONArray("data");
            StringBuilder sb = new StringBuilder(msg);
            if (dataArray != null) {
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONObject item = dataArray.getJSONObject(i);
                    sb.append("\n渠道:").append(item.getStr("channel"))
                            .append(", 流水号:").append(item.getStr("shortCode"))
                            .append(", 状态:").append(item.getStr("message"));
                }
            }
            return sb.toString();
        }
        return "请求失败:" + msg;
    }

    private String validatePushId(SendDto sendDto) {
        if (sendDto == null) {
            return null;
        }
        String template = sendDto.getTemplate();
        if (!StringUtils.hasText(template)) {
            return null;
        }
        if (("form".equals(template) || "doc".equals(template)
                || "excel".equals(template) || "webdiff".equals(template))
                && !StringUtils.hasText(sendDto.getPushId())) {
            return "请求失败: template为" + template + "时，pushId不能为空";
        }
        return null;
    }

}
