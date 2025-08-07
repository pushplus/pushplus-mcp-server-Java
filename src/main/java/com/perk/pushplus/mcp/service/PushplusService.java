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
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PushplusService {

    @Resource
    private PushplusProperties pushplusProperties;

    @Tool(description = "使用pushplus来发送消息;请求成功会返回消息流水号，请求失败返回具体原因")
    public String send(@ToolParam(description = "发送消息参数;包含以下字段：" +
            "token(用户token或消息token，可选)，" +
            "title(消息标题，可选)，" +
            "content(具体消息内容，必填，根据不同template支持不同格式)，" +
            "topic(群组编码，可选，不填仅发送给自己，channel为webhook时无效)，" +
            "template(发送模板，可选，默认值：html，支持：html/txt/json/markdown/cloudMonitor/jenkins/route/pay)，" +
            "channel(发送渠道，可选，默认值：wechat，支持：wechat/webhook/cp/mail/sms)，" +
            "webhook(webhook编码，可选)，" +
            "callbackUrl(发送结果回调地址，可选)，" +
            "timestamp(毫秒时间戳，可选，格式如：1632993318000，服务器时间戳大于此时间戳则消息不会发送)，" +
            "to(好友令牌，可选，微信公众号渠道填写好友令牌，企业微信渠道填写企业微信用户id，多人用逗号隔开)，" +
            "pre(预处理编码，可选)") SendDto sendDto){
        if(sendDto!=null && !StringUtils.hasText(sendDto.getToken())){
            sendDto.setToken(pushplusProperties.getToken());
        }

        HttpResponse response = HttpRequest.post(pushplusProperties.getUrl()).body(JSONUtil.toJsonStr(sendDto)).timeout(2000).execute();
        String result = response.body();

        ResultVo resultVo = JSONUtil.toBean(result, ResultVo.class);
        if(resultVo!=null) {
            String msg = resultVo.getMsg();
            if(resultVo.getCode()==200){
                msg += "消息流水号:" + resultVo.getData();
            }

            return msg;
        } else {
            return "请求失败";
        }
    }

}
