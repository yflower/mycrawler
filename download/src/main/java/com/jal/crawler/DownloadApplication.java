package com.jal.crawler;

import com.jal.crawler.proto.ComponentServer;
import com.jal.crawler.proto.DownloadConfigServer;
import com.jal.crawler.proto.DownloadTaskServer;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class DownloadApplication {


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
//        System.setProperty("webdriver.chrome.driver", "C:\\webdriver\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", "/Users/jianganlan/Downloads/chromedriver");
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
