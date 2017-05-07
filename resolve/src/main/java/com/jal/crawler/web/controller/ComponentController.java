package com.jal.crawler.web.controller;

import com.cufe.taskProcessor.ComponentFacade;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.task.AbstractTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jal.crawler.context.ResolveContext;
import com.jal.crawler.web.param.rpc.ResolveConfigRpcParam;
import com.jal.crawler.web.param.rpc.ResolveTaskOpRpcParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jianganlan on 2017/4/26.
 */
@RestController
@RequestMapping("/component")
public class ComponentController extends ComponentFacade<ResolveConfigRpcParam, ResolveTaskOpRpcParam> {
    private RestTemplate restTemplate = new RestTemplate();

    @Resource(type = ResolveContext.class)
    public void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }


    @GetMapping("/status")
    public Object status() {
        return self();
    }

    @GetMapping(value = "/taskStatus")
    public Object status(@RequestParam(required = false) String taskTag) {
        List<AbstractTask> abstractTasks = taskStatus();
        if (taskTag != null) {
            return abstractTasks.stream().filter(t -> t.getTaskTag().equals(taskTag)).findAny().orElseGet(() -> null);
        }
        return abstractTasks;

    }

    @GetMapping(value = "/taskConfig")
    public Object config(String taskTag) {
        return taskConfig(taskTag);
    }


    @Override
    protected List<ComponentRelation> componentListForward(ComponentRelation leader) {
        String url = "http://" + leader.getHost() + ":8083/resolve/list";
        String object = restTemplate.getForObject(url, String.class);

        List<ComponentRelation> relations = new Gson().fromJson(object,
                new TypeToken<List<ComponentRelation>>() {
                }.getType());
        return relations;
    }


}
