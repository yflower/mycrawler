package com.jal.crawler.web.controller;

import com.jal.crawler.web.biz.DBBiz;
import com.jal.crawler.web.data.apiResponse.ApiResponse;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.param.MongoConfigParam;
import com.jal.crawler.web.data.param.RedisConfigParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * db 配置相关的接口
 * Created by jal on 2017/2/18.
 */
@RestController
public class CommonDBController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDBController.class);

    @Resource
    private DBBiz dbBiz;

    /**
     * 显示当前的db 配置信息
     *
     * @return
     */
    @GetMapping("/db")
    public ApiResponse showDBController() {
        return ApiResponse.successBuild(dbBiz.showDBConfigs());
    }

    /**
     * redis 设置
     *
     * @param redisConfigParam
     * @param bindingResult
     * @return
     */
    @PostMapping("/db/redis")
    public ApiResponse redisConfig(@Valid @RequestBody RedisConfigParam redisConfigParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ApiResponse.failBuild(ExceptionEnum.PARAM_ERROR);
        }
        try {
            dbBiz.dbConfig(redisConfigParam);
        } catch (Exception ex) {
            return ApiResponse.failBuild(ex);
        }
        return ApiResponse.successBuild("");
    }

    /**
     * mongo 设置
     *
     * @param mongoConfigParam
     * @param bindingResult
     * @return
     */

    @PostMapping("/db/mongo")
    public ApiResponse mongoConfig(@Valid @RequestBody MongoConfigParam mongoConfigParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ApiResponse.failBuild(ExceptionEnum.PARAM_ERROR);
        }
        try {
            dbBiz.dbConfig(mongoConfigParam);
        } catch (Exception ex) {
            return ApiResponse.failBuild(ex);
        }
        return ApiResponse.successBuild("");
    }

}
