package com.jal.crawler;

import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.download.DynamicDownload;
import com.jal.crawler.proto.ComponentServer;
import com.jal.crawler.proto.DownloadConfigServer;
import com.jal.crawler.proto.DownloadTaskServer;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisShardInfo;

import javax.jnlp.DownloadService;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@SpringBootApplication
public class DownloadApplication {


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\webdriver\\chromedriver.exe");
        ConfigurableApplicationContext context = SpringApplication.run(DownloadApplication.class, args);
        Server server = ServerBuilder.forPort(9001)
                .addService(context.getBean(DownloadConfigServer.class))
                .addService(context.getBean(DownloadTaskServer.class))
                .addService(context.getBean(ComponentServer.class))
                .build();
        server.start();
        server.awaitTermination();
    }

}
