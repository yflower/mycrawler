package com.jal.crawler.web.controller;

import com.jal.crawler.proto.Clients;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.resolve.ResolveConfig;
import com.jal.crawler.proto.resolve.ResolveTask;
import com.jal.crawler.web.data.VO.ResolveVO;
import com.jal.crawler.web.service.ResolveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jal on 2017/2/8.
 */
@RestController
public class ResolveController {
    @Autowired
    private ResolveService resolveService;


    @GetMapping("/resolves")
    public List<ResolveVO> resolves() {
        return resolveService.status();
    }

    @GetMapping("/resolves/{address}")
    public ResolveVO resolve(@PathVariable("address") String address) {
        return resolveService.status(address);
    }

    @GetMapping("/resolves/test/{num}")
    public String test(@PathVariable int num) {

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

        return "test";
    }
}
