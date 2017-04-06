package com.jal.crawler.web.data.apiResponse;

import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 返回api的统一格式
 * Created by jal on 2017/2/18.
 */
public class ApiResponse<T> {
    private static final Logger LOGGER= LoggerFactory.getLogger(ApiResponse.class);

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


    public static <T> ApiResponse failBuild(ExceptionEnum exceptionEnum, String message) {
        return new ApiResponse(exceptionEnum.getCode(), "exception:" + exceptionEnum.getMessage() + "," + message, "");

    }

    public static <T> ApiResponse failBuild(ExceptionEnum exceptionEnum) {
        return new ApiResponse(exceptionEnum.getCode(), "exception:" + exceptionEnum.getMessage(), "");

    }


    public static <T> ApiResponse failBuild(Exception e) {
        if (e instanceof BizException) {
            return failBuild(((BizException) e).getExceptionEnum(), e.getMessage());
        } else {
            LOGGER.error("error",e);
            return failBuild(ExceptionEnum.UNKNOWN, "");
        }
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
