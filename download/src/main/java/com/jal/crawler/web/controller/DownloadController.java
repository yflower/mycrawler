package com.jal.crawler.web.controller;

import com.cufe.taskProcessor.ComponentFacade;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.task.AbstractTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.task.Task;
import com.jal.crawler.web.convert.WebParamToRpcParam;
import com.jal.crawler.web.param.DownloadConfigParam;
import com.jal.crawler.web.param.DownloadTaskOpParam;
import com.jal.crawler.web.param.rpc.DownloadRpcConfigParam;
import com.jal.crawler.web.param.rpc.DownloadRpcTaskOpParam;
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
@RequestMapping(value = "/download")
public class DownloadController extends ComponentFacade<DownloadRpcConfigParam, DownloadRpcTaskOpParam> {


    private RestTemplate restTemplate = new RestTemplate();

    @Resource(type = DownLoadContext.class)
    public void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }


    @GetMapping(value = "/list")
    public Object list() {
        return componentList();
    }

    @PostMapping(value = "/init")
    public Object init(@RequestBody @Valid DownloadConfigParam params, BindingResult result) {
        if (result.hasErrors()) {
            return "param error";
        }
        componentInit(WebParamToRpcParam.configConvert(params));
        return "";
    }

    @PostMapping(value = "/task")
    public Object taskOp(@RequestBody @Valid DownloadTaskOpParam params, BindingResult result) {
        if (result.hasErrors()) {
            return "param error";
        }
        componentTask(WebParamToRpcParam.taskOpConvert(params), TaskLoadEnum.ALL);
        return "";
    }


    @Override
    protected List<ComponentRelation> componentListForward(ComponentRelation leader) {
        String url = "http://" + leader.getHost() + ":8080/download/list";
        String object = restTemplate.getForObject(url, String.class);

        List<ComponentRelation> relations = new Gson().fromJson(object,
                new TypeToken<List<ComponentRelation>>() {
                }.getType());
        return relations;
    }


}
