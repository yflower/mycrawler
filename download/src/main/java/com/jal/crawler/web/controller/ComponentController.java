package com.jal.crawler.web.controller;

import com.cufe.taskProcessor.ComponentFacade;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.task.AbstractTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.web.param.rpc.DownloadRpcConfigParam;
import com.jal.crawler.web.param.rpc.DownloadRpcTaskOpParam;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jianganlan on 2017/4/26.
 */
@RestController
@RequestMapping("/component")
public class ComponentController extends ComponentFacade<DownloadRpcConfigParam, DownloadRpcTaskOpParam> {
    private RestTemplate restTemplate = new RestTemplate();

    @Resource(type = DownLoadContext.class)
    public void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }



    @GetMapping("/status")
    public Object status() {
        ComponentRelation self = self();
        Map<String,Object> result=new HashMap();
        //common
        result.put("componentType", self.getComponentType());
        result.put("componentRelationType",self.getRelationTypeEnum().getCode());
        result.put("leader",self.getLeader());
        result.put("host",self.getHost());
        result.put("rpcPort", self.getPort());
        result.put("httpPort", self.getServerPort());
        result.put("status",self.getStatus().getCode());
        result.put("thread",componentContext.getThread());

        //download
        DownLoadContext downLoadContext= (DownLoadContext) componentContext;
        result.put("sleepTime",downLoadContext.getSleepTime());
        result.put("isProxy", downLoadContext.isProxy());
        result.put("proxyList",downLoadContext.proxyList());

        return result;

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
    public Object config(String taskTag){
        return taskConfig(taskTag);
    }



    @Override
    protected List<ComponentRelation> componentListForward(ComponentRelation leader) {
        String url = "http://" + leader.getHost() + ":"+leader.getServerPort()+"/download/list";
        String object = restTemplate.getForObject(url, String.class);

        List<ComponentRelation> relations = new Gson().fromJson(object,
                new TypeToken<List<ComponentRelation>>() {
                }.getType());
        return relations;
    }


}
