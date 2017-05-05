package com.jal.crawler.task;

import com.cufe.taskProcessor.task.AbstractTask;
import java.util.*;

/**
 * Created by home on 2017/1/20.
 */
public class Task extends AbstractTask{

    private List<String> linkPattern;


    public List<String> getLinkPattern() {
        return linkPattern;
    }

    public void setLinkPattern(List<String> linkPattern) {
        this.linkPattern = linkPattern;
    }

    @Override
    public void init() {

    }
}
