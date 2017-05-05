package com.jal.crawler.web.param.rpc;

import com.cufe.taskProcessor.ComponentFacade;

import java.util.List;


/**
 * Created by jianganlan on 2017/4/24.
 */
public class LinkTaskOpRpcParam extends ComponentFacade.taskOpParam {
    private boolean test;

    private List<String> linkPattern;


    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public List<String> getLinkPattern() {
        return linkPattern;
    }

    public void setLinkPattern(List<String> linkPattern) {
        this.linkPattern = linkPattern;
    }
}
