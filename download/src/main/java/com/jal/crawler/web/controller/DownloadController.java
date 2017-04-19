package com.jal.crawler.web.controller;

import com.cufe.taskProcessor.ComponentFacade;
import com.cufe.taskProcessor.context.ComponentContext;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.web.param.DownloadConfigParam;
import com.jal.crawler.web.param.DownloadTaskOpParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by jianganlan on 2017/4/19.
 */
@RestController
@RequestMapping(value = "/download")
public class DownloadController extends ComponentFacade<DownloadConfigParam,DownloadTaskOpParam> {


    @Resource(type = DownLoadContext.class)
    public void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }


    @GetMapping(value = "/list")
    public Object list() {
        return componentList();
    }

    @PatchMapping(value = "/init")
    public Object init(@RequestBody DownloadConfigParam params) {
        componentInit(params);
        return "";
    }

    @PatchMapping(value = "/task")
    public Object taskOp(@RequestBody DownloadTaskOpParam params) {
        componentTask(params);
        return "";
    }

    @GetMapping(value = "/taskStatus")
    public Object status() {
        return taskStatus();

    }



}
