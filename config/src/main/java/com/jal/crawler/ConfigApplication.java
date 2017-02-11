package com.jal.crawler;

import com.jal.crawler.proto.Clients;
import com.jal.crawler.proto.config.RedisConfig;
import com.jal.crawler.proto.download.DownloadConfig;
import com.jal.crawler.proto.download.DownloadTask;
import com.jal.crawler.proto.resolve.ResolveConfig;
import com.jal.crawler.proto.resolve.ResolveTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class ConfigApplication {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SpringApplication.run(ConfigApplication.class, args);
    }
}
