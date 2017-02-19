package com.jal.crawler.web.controller;

import com.jal.crawler.proto.Clients;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.resolve.ResolveConfig;
import com.jal.crawler.proto.resolve.ResolveTask;
import com.jal.crawler.web.data.apiResponse.ApiResponse;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.exception.AddressNotFound;
import com.jal.crawler.web.data.exception.ComponentNotFoundException;
import com.jal.crawler.web.data.exception.DBConfigException;
import com.jal.crawler.web.data.exception.ParamErrorException;
import com.jal.crawler.web.data.param.ComponentParam;
import com.jal.crawler.web.data.param.ResolveConfigParam;
import com.jal.crawler.web.service.IResolveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 数据解析组件的相关接口
 * Created by jal on 2017/2/8.
 */
@RestController
public class ResolveController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResolveController.class);


    @Resource
    private IResolveService resolveService;

    /**
     * 得到解析组件的信息
     *
     * @return
     */
    @GetMapping("/resolves/status")
    public ApiResponse resolves(@RequestParam(required = false) List<String> address) {
        return ApiResponse.successBuild(resolveService.status(address));
    }

    /**
     * 添加信息的解析组件
     *
     * @param componentParam
     * @param bindingResult
     * @return
     */
    @PostMapping("/resolves")
    public ApiResponse component(@Valid @RequestBody ComponentParam componentParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ApiResponse.failBuild(ExceptionEnum.PARAM_ERROR, new ParamErrorException("resolve component error"));
        }
        try {
            resolveService.component(componentParam);
        } catch (AddressNotFound addressNotFound) {
            return ApiResponse.failBuild(ExceptionEnum.ADDRESS_NOT_FOUND, addressNotFound);
        }
        return ApiResponse.successBuild("");
    }

    /**
     * 对组件进行初始化
     *
     * @param resolveConfigParam
     * @param bindingResult
     * @return
     */
    @PostMapping("/resolves/config")
    public ApiResponse config(@Valid @RequestBody ResolveConfigParam resolveConfigParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ApiResponse.failBuild(ExceptionEnum.PARAM_ERROR, new ParamErrorException("resolve param error"));
        }
        try {
            resolveService.config(resolveConfigParam);
        } catch (ComponentNotFoundException ex) {
            return ApiResponse.failBuild(ExceptionEnum.COMPONENT_NOT_FOUND, ex);
        } catch (DBConfigException dbConfigEx) {
            return ApiResponse.failBuild(ExceptionEnum.DB_CONFIG_ERROR, dbConfigEx);
        }
        return ApiResponse.successBuild("");
    }

    /**
     * 测试启动组件
     *
     * @param num
     * @return
     */
    @GetMapping("/resolves/test/{num}")
    public ApiResponse test(@PathVariable int num) {

        Clients.resolveClient("localhost", 9005 + num)
                .setConfig(
                        ResolveConfig.newBuilder()
                                .setMongoConfig(
                                        ResolveConfig.MongoConfig.newBuilder()
                                                .setDatabase("test")
                                                .setHost("192.168.1.3")
                                                .setPort(27017)
                                                .setUser("mongo")
                                                .setPassword("zbbJAL86")
                                                .build()
                                )
                                .setPersist(ResolveConfig.Persist.MONGO)
                                .setThread(2)
                                .setRedisConfig(
                                        RedisConfig.newBuilder()
                                                .setHost("192.168.1.3")
                                                .setPort(6379)
                                                .setPassword("zbbJAL86!")
                                                .build()
                                )
                                .build()
                )
                .pushTask(
                        ResolveTask.newBuilder()
                                .setTaskTag("renren")
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("name")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info > div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(1) > td.basic-filed-1 > div > em > a")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("creditScore")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info > div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(1) > td:nth-child(2) > div > em")
                                                .setOption("attr")
                                                .setOptionValue("title")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("age")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info > div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(3) > td.basic-filed-1 > div > em")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("grade")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info > div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(3) > td.basic-filed-2 > div > em")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("marry")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info > div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(3) > td.basic-filed-3 > div > em")
                                                .build()
                                )
                                //
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("loadTimes")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info > div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(5) > td.basic-filed-1 > div > em")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("loadSuccessTimes")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info > div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(6) > td.basic-filed-1 > div > em")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("payedOffTimes")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info > div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(7) > td.basic-filed-1 > div > em")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("creditMoney")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info > div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(5) > td.basic-filed-2 > div > em")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("loadAllMoney")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info > div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(6) > td.basic-filed-2 > div > em")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("waitToPayMoney")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info> div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(7) > td.basic-filed-2 > div > em")
                                                .build()
                                )
                                //
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("delayMoney")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info > div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(5) > td.basic-filed-3 > div > em")
                                                .build()
                                )

                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("delayTimes")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info> div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(6) > td.basic-filed-3 > div > em")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("seriousDelayTimes")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info> div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(7) > td.basic-filed-3 > div > em")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("salary")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info> div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(9) > td.basic-filed-1 > div > em")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("house")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info> div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(9) > td.basic-filed-2 > div > em")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("houseLoad")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info> div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(9) > td.basic-filed-2 > div > em")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("car")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info > div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(10) > td.basic-filed-1 > div > em")
                                                .build()
                                )
                                .addVar(
                                        ResolveTask.Var.newBuilder()
                                                .setName("carLoad")
                                                .setQuery("#loan-tab-content > div > div.ui-tab-content.ui-tab-content-info> div.ui-tab-content-basic.border-bottom.pt25.mlr60 > table > tbody > tr:nth-child(10) > td:nth-child(2) > div > em")
                                                .build()
                                )

                                //
                                .addItem(
                                        ResolveTask.Item.newBuilder()
                                                .setItemName("toubiao")
                                                .addVar(
                                                        ResolveTask.Var.newBuilder()
                                                                .setName("toubiaoren")
                                                                .setQuery("#investment-records > table > tbody > tr:nth-child(1) > td.ui-list-field.text-left > div > a:nth-child(1)")
                                                                .build()
                                                )
                                                .addVar(
                                                        ResolveTask.Var.newBuilder()
                                                                .setName("toubiaojiner")
                                                                .setQuery("#investment-records > table > tbody > tr:nth-child(1) > td:nth-child(3) > div > em")
                                                                .build()
                                                )
                                                .addVar(
                                                        ResolveTask.Var.newBuilder()
                                                                .setName("toubiaoshijian")
                                                                .setQuery("#investment-records > table > tbody > tr:nth-child(1) > td:nth-child(4) > div")
                                                                .build()
                                                )
                                                .build()
                                )

                                .build());

        return ApiResponse.successBuild("test");
    }
}
