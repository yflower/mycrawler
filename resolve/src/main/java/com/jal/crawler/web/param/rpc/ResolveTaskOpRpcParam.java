package com.jal.crawler.web.param.rpc;

import com.cufe.taskProcessor.ComponentFacade;
import com.jal.crawler.task.Task;

import java.util.List;

/**
 * Created by jianganlan on 2017/4/24.
 */
public class ResolveTaskOpRpcParam extends ComponentFacade.taskOpParam {
    private boolean test;

    private List<Task.var> vars;

    private List<Task.item> items;


    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public List<Task.var> getVars() {
        return vars;
    }

    public void setVars(List<Task.var> vars) {
        this.vars = vars;
    }

    public List<Task.item> getItems() {
        return items;
    }

    public void setItems(List<Task.item> items) {
        this.items = items;
    }



}
