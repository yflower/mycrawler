package com.jal.crawler.web.controller;

import com.jal.crawler.proto.Clients;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.download.DownloadConfig;
import com.jal.crawler.proto.download.DownloadTask;
import com.jal.crawler.web.data.apiResponse.ApiResponse;
import com.jal.crawler.web.data.enums.ExceptionEnum;
import com.jal.crawler.web.data.exception.AddressNotFound;
import com.jal.crawler.web.data.exception.ComponentNotFoundException;
import com.jal.crawler.web.data.exception.DBConfigException;
import com.jal.crawler.web.data.exception.ParamErrorException;
import com.jal.crawler.web.data.param.ComponentParam;
import com.jal.crawler.web.data.param.DownloadConfigParam;
import com.jal.crawler.web.service.IDownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jal on 2017/2/10.
 * 下载组件相关的api接口
 */
@RestController
public class DownloadController {
    private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);


    @Resource
    private IDownloadService downloadService;


    /**
     * 得到所有下载组件的信息
     *
     * @return
     */
    @GetMapping("/downloads/status")
    public ApiResponse view(@RequestParam(required = false) List<String> address) {
        return ApiResponse.successBuild(downloadService.status(address));

    }

    /**
     * 添加新的节点
     *
     * @param param
     * @param bindingResult
     * @return
     */
    @PostMapping("/downloads")
    public ApiResponse component(@Valid @RequestBody ComponentParam param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ApiResponse.failBuild(ExceptionEnum.PARAM_ERROR, new ParamErrorException("download component error"));
        }
        try {
            downloadService.component(param);
        } catch (AddressNotFound addressNotFound) {
            return ApiResponse.failBuild(ExceptionEnum.ADDRESS_NOT_FOUND, addressNotFound);
        }
        return ApiResponse.successBuild("");

    }


    /**
     * 添加新的节点并初始化
     *
     * @param downloadConfigParam
     * @param bindingResult
     * @return
     */
    @PostMapping("/downloads/config")
    public ApiResponse config(@Valid @RequestBody DownloadConfigParam downloadConfigParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ApiResponse.failBuild(ExceptionEnum.PARAM_ERROR, new ParamErrorException("download param error"));
        }
        try {
            downloadService.config(downloadConfigParam);
        } catch (ComponentNotFoundException ex) {
            return ApiResponse.failBuild(ExceptionEnum.COMPONENT_NOT_FOUND, ex);
        } catch (DBConfigException dbConfigEx) {
            return ApiResponse.failBuild(ExceptionEnum.DB_CONFIG_ERROR, dbConfigEx);
        }
        return ApiResponse.successBuild("");
    }

    /**
     * 暂时的测试开始任务
     *
     * @param num
     * @return
     */
    @GetMapping("/downloads/test/{num}")
    public ApiResponse test(@PathVariable int num) {
        if (num == 1) {
            Clients.downloadClient("localhost", 9000 + num)
                    .setConfig(DownloadConfig.newBuilder()
                            .setThread(10)
                            .setPersist(DownloadConfig.Persist.REDIS)
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
                            DownloadTask.newBuilder()
                                    .setTaskTag("renren")
                                    .setDynamic(true)
                                    .addPre(
                                            DownloadTask.Processor.newBuilder()
                                                    .setOrder(0)
                                                    .setType(DownloadTask.Processor.Type.LINK_TO)
                                                    .setQuery("https://www.we.com/loginPage.action")
                                                    .build()
                                    )
                                    .addPre(
                                            DownloadTask.Processor.newBuilder()
                                                    .setOrder(1)
                                                    .setType(DownloadTask.Processor.Type.INPUT)
                                                    .setQuery("#j_username")
                                                    .setValue("18810397253")
                                                    .build()
                                    )
                                    .addPre(
                                            DownloadTask.Processor.newBuilder()
                                                    .setOrder(2)
                                                    .setType(DownloadTask.Processor.Type.INPUT)
                                                    .setQuery("#J_pass_input")
                                                    .setValue("abc1234567890")
                                                    .build()
                                    )
                                    .addPre(
                                            DownloadTask.Processor.newBuilder()
                                                    .setOrder(3)
                                                    .setType(DownloadTask.Processor.Type.INPUT_SUBMIT)
                                                    .setQuery("#login > fieldset > div:nth-child(8) > input")
                                                    .build()
                                    )
                                    .addPost(
                                            DownloadTask.Processor.newBuilder()
                                                    .setOrder(0)
                                                    .setType(DownloadTask.Processor.Type.CLICK)
                                                    .setQuery("#loan-tab > ul > li:nth-child(2) > a")
                                                    .build()
                                    )
                                    .addPost(
                                            DownloadTask.Processor.newBuilder()
                                                    .setOrder(1)
                                                    .setType(DownloadTask.Processor.Type.WAIT_UTIL)
                                                    .setQuery("#investment-records > table > tbody > tr:nth-child(1) > td.ui-list-field.text-left > div > a:nth-child(1)")
                                                    .build()
                                    )
                                    .addAllStartUrl(new ArrayList<String>() {{
                                        for (int i = 210_0000; i <= 213_5220; ++i) {
                                            add("https://www.we.com/loan/" + i);
                                        }
                                    }})
                                    .build()


                    );
        } else {
            Clients.downloadClient("192.168.1.30", 9000 + num)
                    .setConfig(DownloadConfig.newBuilder()
                            .setThread(1)
                            .setPersist(DownloadConfig.Persist.REDIS)
                            .setSleepTime(100)
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
                            DownloadTask.newBuilder()
                                    .setTaskTag("page")
                                    .setDynamic(true)
                                    .addPre(
                                            DownloadTask.Processor.newBuilder()
                                                    .setOrder(0)
                                                    .setType(DownloadTask.Processor.Type.LINK_TO)
                                                    .setQuery("https://www.we.com/loginPage.action")
                                                    .build()
                                    )
                                    .addPre(
                                            DownloadTask.Processor.newBuilder()
                                                    .setOrder(1)
                                                    .setType(DownloadTask.Processor.Type.INPUT)
                                                    .setQuery("#j_username")
                                                    .setValue("18810397253")
                                                    .build()
                                    )
                                    .addPre(
                                            DownloadTask.Processor.newBuilder()
                                                    .setOrder(2)
                                                    .setType(DownloadTask.Processor.Type.INPUT)
                                                    .setQuery("#J_pass_input")
                                                    .setValue("abc1234567890")
                                                    .build()
                                    )
                                    .addPre(
                                            DownloadTask.Processor.newBuilder()
                                                    .setOrder(3)
                                                    .setType(DownloadTask.Processor.Type.INPUT_SUBMIT)
                                                    .setQuery("#login > fieldset > div:nth-child(8) > input")
                                                    .build()
                                    )
                                    .addPost(
                                            DownloadTask.Processor.newBuilder()
                                                    .setOrder(0)
                                                    .setType(DownloadTask.Processor.Type.CLICK)
                                                    .setQuery("#loan-tab > ul > li:nth-child(2) > a")
                                                    .build()
                                    )
                                    .addPost(
                                            DownloadTask.Processor.newBuilder()
                                                    .setOrder(1)
                                                    .setType(DownloadTask.Processor.Type.WAIT_UTIL)
                                                    .setQuery("#investment-records > table > tbody > tr:nth-child(1) > td.ui-list-field.text-left > div > a:nth-child(1)")
                                                    .build()
                                    )
                                    .build()


                    );
        }

        return ApiResponse.successBuild("test");
    }
}
