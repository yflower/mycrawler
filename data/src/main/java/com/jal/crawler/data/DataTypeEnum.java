package com.jal.crawler.data;

/**
 * Created by jianganlan on 2017/5/2.
 */
public enum DataTypeEnum {
    JSON_STRING     (0,"json格式字符串"),
    TXT_STRING      (1,"将json格式参开的按照空格分开的行字符串"),
    EXCEL           (2,"将json格式参开")
    ;
    private int type;
    private String msg;

    DataTypeEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public static DataTypeEnum numberOf(int dataType){
        for(DataTypeEnum dataTypeEnum:DataTypeEnum.values()){
            if(dataTypeEnum.getType()==dataType){
                return dataTypeEnum;
            }
        }
        throw new IllegalStateException("无法转化异常");
    }
}
