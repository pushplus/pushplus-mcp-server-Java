package com.perk.pushplus.mcp.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.perk.pushplus.mcp.dto.SendDto;
import com.perk.pushplus.mcp.properties.PushplusProperties;
import com.perk.pushplus.mcp.vo.ResultVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class PushplusService {

    @Resource
    private PushplusProperties pushplusProperties;

    @Tool(description = "使用pushplus来发送消息;请求成功会返回消息流水号，请求失败返回具体原因")
    public String send(@ToolParam(description = "发送消息参数") SendDto sendDto){
        if(sendDto!=null && StringUtils.isEmpty(sendDto.getToken())){
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
