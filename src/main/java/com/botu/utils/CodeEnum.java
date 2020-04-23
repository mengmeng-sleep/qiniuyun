package com.botu.utils;

import lombok.Getter;

/**
 * 请求响应状态码
 * @ClassName CodeEmum
 * @Author ZX
 * @Date 2020/4/7 10:01
 * @Version 1.0.0
 **/
@Getter
public enum CodeEnum {

    SUCCESS_CODE(200, "请求成功");


    private int code;

    private String message;

    CodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
