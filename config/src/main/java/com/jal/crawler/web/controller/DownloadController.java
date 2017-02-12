package com.jal.crawler.web.controller;

import com.jal.crawler.proto.Clients;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.download.DownloadConfig;
import com.jal.crawler.proto.download.DownloadTask;
import com.jal.crawler.web.data.VO.DownloadVO;
import com.jal.crawler.web.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jal on 2017/2/10.
 */
@RestController
public class DownloadController {
    @Autowired
    private DownloadService downloadService;

    @GetMapping("/downloads")
    public List<DownloadVO> downloadVOList() {
        return downloadService.status();

    }

    @GetMapping("/downloads/{address}")
    public DownloadVO downloadVO(@PathVariable String address) {
        return downloadService.status(address);
    }

    @GetMapping("/downloads/test/{num}")
    public String test(@PathVariable int num) {
        if (num == 1) {
            Clients.DownloadClient("localhost", 9000 + num)
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
            Clients.DownloadClient("192.168.1.30", 9000 + num)
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

        return "test";
    }
}
