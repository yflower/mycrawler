package com.jal.crawler.data;

/**
 * Created by jianganlan on 2017/5/2.
 */
public class Data {
    private DataTypeEnum dataTypeEnum;

    private Object data;

    public DataTypeEnum getDataTypeEnum() {
        return dataTypeEnum;
    }

    public void setDataTypeEnum(DataTypeEnum dataTypeEnum) {
        this.dataTypeEnum = dataTypeEnum;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
