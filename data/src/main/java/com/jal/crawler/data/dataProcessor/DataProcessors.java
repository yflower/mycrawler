package com.jal.crawler.data.dataProcessor;

import com.jal.crawler.data.DataTypeEnum;

/**
 * Created by jianganlan on 2017/5/2.
 */
public class DataProcessors {
    public static DataProcessor of(DataTypeEnum dataTypeEnum) {
        switch (dataTypeEnum) {
            case EXCEL_FILE:
                return new ExcelFileDataProcessor();
            case TXT_STRING_FILE:
                return new TXTFileDataProcessor();
            case JSON_STRING_FILE:
                return new JSONFileDataProcessor();
            case JSON:
                return new JSONDataProcessor();
            default:
                throw new IllegalStateException("未知的数据处理类型");

        }
    }
}
