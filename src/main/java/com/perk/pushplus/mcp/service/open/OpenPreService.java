package com.perk.pushplus.mcp.service.open;

import cn.hutool.json.JSONObject;
import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class OpenPreService extends OpenToolSupport {

    public OpenPreService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "POST /open/pre/add - 新增预处理(需会员)。必填: content,preName,preCode,contentType(1-JavaScript)。"
            + OpenApiDocs.RESULT + "data: 新建预处理编号。")
    public String openPreAdd(
            @ToolParam(description = "预处理名称") String preName,
            @ToolParam(description = "预处理编码") String preCode,
            @ToolParam(description = "预处理代码") String content,
            @ToolParam(description = "编程语言类型；1-JavaScript") Integer contentType) {
        JSONObject body = new JSONObject();
        body.set("preName", preName);
        body.set("preCode", preCode);
        body.set("content", content);
        body.set("contentType", contentType);
        return run(() -> openApiClient.post("/open/pre/add", body));
    }

    @Tool(description = "POST /open/pre/list - 预处理列表(需会员)。"
            + OpenApiDocs.PAGE_REQ + OpenApiDocs.RESULT + OpenApiDocs.PAGE_RESP
            + "list项: id,preName,preCode,contentType(1-JavaScript),createTime。")
    public String openPreList(
            @ToolParam(description = "当前所在分页数，默认1", required = false) Integer current,
            @ToolParam(description = "每页大小，默认20，最大50", required = false) Integer pageSize) {
        return run(() -> openApiClient.post("/open/pre/list", pageBody(current, pageSize, null)));
    }

    @Tool(description = "GET /open/pre/detail - 预处理详情(需会员)。"
            + OpenApiDocs.RESULT + "data: id,preName,preCode,contentType,content。")
    public String openPreDetail(@ToolParam(description = "预处理信息编号") Long preId) {
        return run(() -> openApiClient.get("/open/pre/detail", query("preId", preId)));
    }

    @Tool(description = "DELETE /open/pre/delete - 高风险：删除预处理(需会员)。" + OpenApiDocs.RESULT)
    public String openPreDelete(@ToolParam(description = "预处理信息编号") Long preId) {
        return run(() -> openApiClient.delete("/open/pre/delete", query("preId", preId)));
    }

    @Tool(description = "POST /open/pre/edit - 修改预处理(需会员)。必填: id,content,preName,preCode,contentType。"
            + OpenApiDocs.RESULT + "data: 如修改成功。")
    public String openPreEdit(
            @ToolParam(description = "预处理信息编号") Long id,
            @ToolParam(description = "预处理名称") String preName,
            @ToolParam(description = "预处理编码") String preCode,
            @ToolParam(description = "预处理代码") String content,
            @ToolParam(description = "编程语言类型；1-JavaScript") Integer contentType) {
        JSONObject body = new JSONObject();
        body.set("id", id);
        body.set("preName", preName);
        body.set("preCode", preCode);
        body.set("content", content);
        body.set("contentType", contentType);
        return run(() -> openApiClient.post("/open/pre/edit", body));
    }

    @Tool(description = "POST /open/pre/test - 测试预处理(需会员)。必填: content,contentType,message。"
            + OpenApiDocs.RESULT + "data: 预处理后的消息内容。")
    public String openPreTest(
            @ToolParam(description = "测试消息内容") String message,
            @ToolParam(description = "预处理代码") String content,
            @ToolParam(description = "编程语言类型；1-JavaScript") Integer contentType) {
        JSONObject body = new JSONObject();
        body.set("message", message);
        body.set("content", content);
        body.set("contentType", contentType);
        return run(() -> openApiClient.post("/open/pre/test", body));
    }
}
