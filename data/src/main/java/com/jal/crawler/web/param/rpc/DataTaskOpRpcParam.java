package com.jal.crawler.web.param.rpc;

import com.cufe.taskProcessor.ComponentFacade;


/**
 * Created by jianganlan on 2017/4/24.
 */
public class DataTaskOpRpcParam extends ComponentFacade.taskOpParam {
    private boolean test;

    private int dataType;


    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
}
