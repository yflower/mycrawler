package com.jal.crawler.web.data.model.task;

import com.jal.crawler.web.data.enums.ComponentEnum;

import java.util.List;

/**
 * Created by jianganlan on 2017/5/5.
 */
public class LinkOperationModel extends TaskOperationModel {
    private List<String> linkPattern;

    public LinkOperationModel(String taskTag) {
        this.setTaskTag(taskTag);
        this.setComponentType(ComponentEnum.LINK);
    }

    public List<String> getLinkPattern() {
        return linkPattern;
    }

    public void setLinkPattern(List<String> linkPattern) {
        this.linkPattern = linkPattern;
    }
}
