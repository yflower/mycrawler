package com.jal.crawler.data;

import org.springframework.http.ResponseEntity;

/**
 * Created by jianganlan on 2017/5/2.
 */
public class Data {
    private DataTypeEnum dataTypeEnum;

    private ResponseEntity response;

    public DataTypeEnum getDataTypeEnum() {
        return dataTypeEnum;
    }

    public void setDataTypeEnum(DataTypeEnum dataTypeEnum) {
        this.dataTypeEnum = dataTypeEnum;
    }

    public ResponseEntity getResponse() {
        return response;
    }

    public void setResponse(ResponseEntity response) {
        this.response = response;
    }
}
