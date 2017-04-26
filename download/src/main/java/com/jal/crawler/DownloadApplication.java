package com.jal.crawler;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.rpc.*;
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
        Integer type = Integer.valueOf(args[0]);
        Integer port = Integer.valueOf(args[1]);
        String host = args[2];

//        System.setProperty("webdriver.chrome.driver", "C:\\webdriver\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", "/Users/jianganlan/Downloads/chromedriver");
        ConfigurableApplicationContext context = SpringApplication.run(DownloadApplication.class, args);
        Server server = ServerBuilder.forPort(port)
                .addService(context.getBean(DownloadInitServer.class))
                .addService(context.getBean(DownloadTaskServer.class))
                .addService(context.getBean(DownloadStatusServer.class))
                .addService(context.getBean(DownloadLeaderServer.class))
                .addService(context.getBean(DownloadHeartServer.class))
                .build();


        ComponentRelation self = new ComponentRelation();

        self.setHost(host);
        self.setLeader(self);
        self.setStatus(StatusEnum.NO_INIT);
        self.setRelationTypeEnum(ComponentRelationTypeEnum.numberOf(type));
        self.setPort(port);
        self.setComponentType(0);

        DownLoadContext loadContext = context.getBean(DownLoadContext.class);

        loadContext.componentStart(self, self);


        server.start();
        server.awaitTermination();


    }

}
