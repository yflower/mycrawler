package com.jal.crawler;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.cufe.taskProcessor.task.StatusEnum;
import com.cufe.taskRpc.RpcUtils;
import com.jal.crawler.context.LinkContext;
import com.jal.crawler.enums.ComponentTypeEnum;
import com.jal.crawler.rpc.LinkInitServer;
import com.jal.crawler.rpc.LinkTaskServer;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class LinkApplication {

    public static void main(String[] args) throws IOException, InterruptedException {

        Integer type = Integer.valueOf(args[0]);
        Integer port = Integer.valueOf(args[1]);
        String host = args[2];
        ConfigurableApplicationContext context = SpringApplication.run(LinkApplication.class, args);

        LinkContext loadContext = context.getBean(LinkContext.class);
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(port)
                .addService(context.getBean(LinkInitServer.class))
                .addService(context.getBean(LinkTaskServer.class));


        Server server = RpcUtils.registServer(serverBuilder, loadContext).build();


        ComponentRelation self = new ComponentRelation();

        self.setHost(host);
        self.setStatus(StatusEnum.NO_INIT);
        self.setRelationTypeEnum(ComponentRelationTypeEnum.numberOf(type));
        self.setPort(port);
        self.setComponentType(ComponentTypeEnum.DOWNLOAD.getCode());


        loadContext.componentStart(self, type == 0 ? self : null);


        server.start();
        server.awaitTermination();
    }
}
