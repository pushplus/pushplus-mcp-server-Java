package com.perk.pushplus.mcp.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResultVo<T> implements Serializable {

    @Schema(description = "响应码")
    private String msg;

    @Schema(description = "响应说明")
    private Integer code;

    @Schema(description = "响应数据")
    private T data;

}
