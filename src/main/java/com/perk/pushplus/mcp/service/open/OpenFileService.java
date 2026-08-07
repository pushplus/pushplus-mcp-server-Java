package com.perk.pushplus.mcp.service.open;

import com.perk.pushplus.mcp.client.OpenApiClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class OpenFileService extends OpenToolSupport {

    public OpenFileService(OpenApiClient openApiClient) {
        super(openApiClient);
    }

    @Tool(description = "GET /open/userImage/uploadToken - 获取七牛上传凭证(图片服务,30天有效)。"
            + OpenApiDocs.RESULT
            + "data: uploadToken,uploadHost,uploadUrl,bucket,expiresIn。"
            + "拿到凭证后向uploadUrl表单提交token+file(无需access-key)。")
    public String openUserImageUploadToken() {
        return run(() -> openApiClient.get("/open/userImage/uploadToken", null));
    }

    @Tool(description = "POST /open/file/uploadImage - 通过access-key直接上传图片。"
            + "入参filename+contentBase64由MCP组装multipart。"
            + "官方推荐七牛直传请先调openUserImageUploadToken。"
            + OpenApiDocs.RESULT + "data一般为图片URL。")
    public String openFileUploadImage(
            @ToolParam(description = "文件名，如image.png") String filename,
            @ToolParam(description = "图片Base64内容（不含data:前缀）") String contentBase64) {
        return run(() -> openApiClient.uploadImage(filename, contentBase64));
    }

    @Tool(description = "POST /open/userImage/list - 图片列表。未删除图片默认30天后清理。"
            + OpenApiDocs.PAGE_REQ + OpenApiDocs.RESULT + OpenApiDocs.PAGE_RESP
            + "list项: id,imgUrl,thumbnail,createTime。")
    public String openUserImageList(
            @ToolParam(description = "当前所在分页数，默认1", required = false) Integer current,
            @ToolParam(description = "每页大小，默认20，最大50", required = false) Integer pageSize) {
        return run(() -> openApiClient.post("/open/userImage/list", pageBody(current, pageSize, null)));
    }

    @Tool(description = "DELETE /open/userImage/delete - 高风险：主动删除图片。" + OpenApiDocs.RESULT)
    public String openUserImageDelete(@ToolParam(description = "图片id") Long id) {
        return run(() -> openApiClient.delete("/open/userImage/delete", query("id", id)));
    }
}
