package com.jal.crawler.web.data.apiResponse;

import com.jal.crawler.web.data.enums.ExceptionEnum;

/**
 * 返回api的统一格式
 * Created by jal on 2017/2/18.
 */
public class ApiResponse<T> {
    private String code;

    private String message;

    private T data;

    private ApiResponse(String code, String message, T data) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public static <T> ApiResponse successBuild(T data) {
        return new ApiResponse("200", "success", data);
    }


    public static <T> ApiResponse failBuild(ExceptionEnum exceptionEnum, Exception exception) {
        return new ApiResponse(exceptionEnum.getCode(), "exception:" + exceptionEnum.getMessage() + "," + exception.getMessage(), "");

    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
