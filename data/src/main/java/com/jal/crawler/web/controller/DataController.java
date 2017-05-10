package com.jal.crawler.web.controller;

import com.cufe.taskProcessor.ComponentFacade;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.context.ComponentContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jal.crawler.context.DataContext;
import com.jal.crawler.data.Data;
import com.jal.crawler.web.convert.WebParamToRpcParam;
import com.jal.crawler.web.param.DataConfigParam;
import com.jal.crawler.web.param.DataTaskOpParam;
import com.jal.crawler.web.param.rpc.DataConfigRpcParam;
import com.jal.crawler.web.param.rpc.DataTaskOpRpcParam;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by jianganlan on 2017/4/19.
 */
@RestController
@RequestMapping(value = "/data")
public class DataController extends ComponentFacade<DataConfigRpcParam, DataTaskOpRpcParam> {


    private RestTemplate restTemplate = new RestTemplate();

    @Resource(type = DataContext.class)
    public void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }


    @GetMapping(value = "/list")
    public Object list() {
        return componentList();
    }

    @PostMapping(value = "/init")
    public Object init(@RequestBody @Valid DataConfigParam params, BindingResult result) {
        if (result.hasErrors()) {
            return "param error";
        }
        componentInit(WebParamToRpcParam.configConvert(params));
        return "";
    }

    @PostMapping(value = "/task")
    public Object taskOp(@RequestBody @Valid DataTaskOpParam params, BindingResult result) {
        if (result.hasErrors()) {
            return "param error";
        }
        componentTask(WebParamToRpcParam.taskOpConvert(params), TaskLoadEnum.ALL);

        return "";
    }

    @GetMapping(value = "/result")
    public ResponseEntity result(@RequestParam("taskTag") String taskTag, @RequestParam("type") int type) throws IOException {

        DataContext dataContext = (DataContext) componentContext;
        Map<String, Data> finishResult = dataContext.getFinishResult();

        Function<String, ResponseEntity> result = t -> {
            Data data = finishResult.get(taskTag);
            finishResult.remove(taskTag);
            return data.getResponse();
        };

        if (finishResult.containsKey(taskTag) && finishResult.get(taskTag).getDataTypeEnum().getType() == type) {
            return result.apply(taskTag);
        } else {
            int retryTime = 5;
            for (int i = 0; i < retryTime; ++i) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (finishResult.containsKey(taskTag) && finishResult.get(taskTag).getDataTypeEnum().getType() == type) {
                    return result.apply(taskTag);
                }
            }
            return ResponseEntity.notFound().build();

        }

    }


    @GetMapping(value = "/taskStatus")
    public Object status() {
        return taskStatus();

    }


    @Override
    protected List<ComponentRelation> componentListForward(ComponentRelation leader) {
        String url = "http://"+leader.getHost() + ":8084/data/list";
        ResponseEntity<String> string = restTemplate.getForEntity(url, String.class);

        List<ComponentRelation> relations = new Gson().fromJson(string.getBody(),
                new TypeToken<List<ComponentRelation>>() {
                }.getType());
        return relations;
    }


}
