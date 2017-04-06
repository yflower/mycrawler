package com.jal.crawler.web.data.exception;

import com.jal.crawler.web.data.enums.ExceptionEnum;

/**
 * Created by jal on 2017/2/25.
 */
public class BizException extends RuntimeException {
    private ExceptionEnum exceptionEnum;

    private String message;

    public BizException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
        message = "";

    }

    public BizException(ExceptionEnum exceptionEnum, String message) {
        this.exceptionEnum = exceptionEnum;
        this.message = message;
    }

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
