package com.jal.crawler.web.controller;

import com.cufe.taskProcessor.ComponentFacade;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.task.AbstractTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jal.crawler.context.ResolveContext;
import com.jal.crawler.task.Task;
import com.jal.crawler.web.convert.WebParamToRpcParam;
import com.jal.crawler.web.param.ResolveConfigParam;
import com.jal.crawler.web.param.ResolveTaskOpParam;
import com.jal.crawler.web.param.rpc.ResolveConfigRpcParam;
import com.jal.crawler.web.param.rpc.ResolveTaskOpRpcParam;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jianganlan on 2017/4/19.
 */
@RestController
@RequestMapping(value = "/resolve")
public class ResolveController extends ComponentFacade<ResolveConfigRpcParam, ResolveTaskOpRpcParam> {


    private RestTemplate restTemplate = new RestTemplate();

    @Resource(type = ResolveContext.class)
    public void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }


    @GetMapping(value = "/list")
    public Object list() {
        return componentList();
    }

    @PostMapping(value = "/init")
    public Object init(@RequestBody @Valid ResolveConfigParam params, BindingResult result) {
        if (result.hasErrors()) {
            return "param error";
        }
        componentInit(WebParamToRpcParam.configConvert(params));
        return "";
    }

    @PostMapping(value = "/task")
    public Object taskOp(@RequestBody @Valid ResolveTaskOpParam params, BindingResult result) {
        if (result.hasErrors()) {
            return "param error";
        }
        componentTask(WebParamToRpcParam.taskOpConvert(params),TaskLoadEnum.ALL);
        return "";
    }

    @GetMapping(value = "/taskStatus")
    public Object status() {
        return taskStatus();

    }


    @Override
    protected List<ComponentRelation> componentListForward(ComponentRelation leader) {
        String url = leader.getHost() + ":8080/resolve/list";
        ResponseEntity<String> string = restTemplate.getForEntity(url, String.class);

        List<ComponentRelation> relations = new Gson().fromJson(string.getBody(),
                new TypeToken<List<ComponentRelation>>() {
                }.getType());
        return relations;
    }


}
