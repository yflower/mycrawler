package com.jal.crawler;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.cufe.taskProcessor.task.StatusEnum;
import com.cufe.taskRpc.RpcUtils;
import com.jal.crawler.context.DownLoadContext;
import com.jal.crawler.enums.ComponentTypeEnum;
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
        Integer type = Integer.valueOf(args[0]);
        String host = args[1];
        Integer httpPort = Integer.valueOf(args[2]);
        Integer port = Integer.valueOf(args[3]);

        String os = System.getProperty("os.name");

        String baseUrl = DownloadApplication.class.getResource("/webdriver/").getPath();

        String phantomjs=baseUrl+"phantomjs_"+os;



        System.setProperty("phantomjs.binary.path", "/tmp/phantomjs");
        System.setProperty("webdriver.chrome.driver", "/Users/jianganlan/Downloads/chromedriver");
        ConfigurableApplicationContext context = SpringApplication.run(DownloadApplication.class, args);

        DownLoadContext loadContext = context.getBean(DownLoadContext.class);
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(port)
                .addService(context.getBean(DownloadInitServer.class))
                .addService(context.getBean(DownloadTaskServer.class));


        Server server = RpcUtils.registServer(serverBuilder, loadContext).build();


        ComponentRelation self = new ComponentRelation();

        self.setHost(host);
        self.setStatus(StatusEnum.NO_INIT);
        self.setRelationTypeEnum(ComponentRelationTypeEnum.numberOf(type));
        self.setPort(port);
        self.setServerPort(httpPort);
        self.setComponentType(loadContext.componentType());


        loadContext.componentStart(self, type == 0 ? self : null);


        server.start();
        server.awaitTermination();


    }

}
