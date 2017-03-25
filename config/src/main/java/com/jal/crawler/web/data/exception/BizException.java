package com.jal.crawler.web.data.exception;

import com.jal.crawler.web.data.enums.ExceptionEnum;

/**
 * Created by jal on 2017/2/25.
 */
public class BizException extends RuntimeException {
    private ExceptionEnum exceptionEnum;

    public BizException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }
}
