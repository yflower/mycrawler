package com.jal.crawler.data.dataProcessor;

import com.jal.crawler.data.DataTypeEnum;

/**
 * Created by jianganlan on 2017/5/2.
 */
public class DataProcessors {
    public static DataProcessor of(DataTypeEnum dataTypeEnum) {
        switch (dataTypeEnum) {
            case EXCEL:
                return new ExcelDataProcessor();
            case TXT_STRING:
                return new TXTDataProcessor();
            case JSON_STRING:
                return new JSONDataProcessor();
            default:
                return new TXTDataProcessor();
        }
    }
}
