package com.jal.crawler.web.controller;

import com.jal.crawler.web.biz.ComponentBiz;
import com.jal.crawler.web.data.apiResponse.ApiResponse;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.exception.BizException;
import com.jal.crawler.web.data.param.ComponentParam;
import com.jal.crawler.web.data.param.DownloadParam;
import com.jal.crawler.web.data.param.ResolveParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by jal on 2017/2/25.
 */
@RestController
@RequestMapping("/components")
@CrossOrigin
public class ComponentController {
    public static final Logger LOGGER = LoggerFactory.getLogger(ComponentController.class);

    @Resource
    private ComponentBiz componentBiz;

    /**
     * 得到所有下载组件的信息
     *
     * @return
     */
    @GetMapping("/status")
    public ApiResponse view(@RequestParam(required = false) ComponentParam componentParams) {
        return ApiResponse.successBuild(componentBiz.status(componentParams));
    }

    /**
     * 添加新的节点
     *
     * @param param
     * @param bindingResult
     * @return
     */
    @PostMapping
    public ApiResponse component(@Valid @RequestBody ComponentParam param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BizException(ExceptionEnum.PARAM_ERROR);
        }
        componentBiz.component(param);
        return ApiResponse.successBuild("");

    }


    /**
     * 添加自定义设置的下载组件
     *
     * @param downloadParam
     * @param bindingResult
     * @return
     */
    @PostMapping("/download")
    public ApiResponse downloadConfig(@Valid @RequestBody DownloadParam downloadParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BizException(ExceptionEnum.PARAM_ERROR);
        }
        componentBiz.component(downloadParam);
        return ApiResponse.successBuild("");
    }


    /**
     * 添加制定设置的解析组件
     *
     * @param resolveParam
     * @param bindingResult
     * @return
     */
    @PostMapping("/resolve")
    public ApiResponse resolveConfig(@Valid @RequestBody ResolveParam resolveParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BizException(ExceptionEnum.PARAM_ERROR);
        }
        componentBiz.component(resolveParam);
        return ApiResponse.successBuild("");
    }


}
