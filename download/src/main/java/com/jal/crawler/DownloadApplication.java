package com.jal.crawler;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.rpc.DownloadStatusServer;
import com.jal.crawler.rpc.DownloadInitServer;
import com.jal.crawler.rpc.DownloadTaskServer;
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
                .addService(context.getBean(DownloadInitServer.class))
                .addService(context.getBean(DownloadTaskServer.class))
                .addService(context.getBean(DownloadStatusServer.class))
                .build();


        ComponentRelation self=new ComponentRelation();

        self.setRelationTypeEnum(ComponentRelationTypeEnum.LEADER);
        self.setHost("127.0.0.1");
        self.setLeader(self);
        self.setPort(9001);

        DownLoadContext loadContext = context.getBean(DownLoadContext.class);

        loadContext.componentStart(self,self);


        server.start();
        server.awaitTermination();


    }

}
