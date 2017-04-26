package com.jal.crawler.web.controller;

import com.jal.crawler.web.data.apiResponse.ApiResponse;
import com.jal.crawler.web.data.exception.BizException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by jal on 2017/4/9.
 * 全局的controller异常捕获
 * 反倒是反倒是发的
 */
@RestControllerAdvice
public class ControllerExceptionHandle {
    @ExceptionHandler(BizException.class)
    public ApiResponse bizException(BizException bizException) {
        return ApiResponse.failBuild(bizException);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse exception(Exception e) {
        return ApiResponse.failBuild(e);
    }
}
