package com.jal.crawler.web.data.model.task;

import com.jal.crawler.web.data.enums.DataTypeEnum;

/**
 * Created by jianganlan on 2017/5/3.
 */
public class DataOperationModel extends TaskOperationModel {
    private DataTypeEnum dataTypeEnum;

    public DataTypeEnum getDataTypeEnum() {
        return dataTypeEnum;
    }

    public void setDataTypeEnum(DataTypeEnum dataTypeEnum) {
        this.dataTypeEnum = dataTypeEnum;
    }
}
