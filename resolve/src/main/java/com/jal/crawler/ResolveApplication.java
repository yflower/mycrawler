package com.jal.crawler;


import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.cufe.taskProcessor.task.StatusEnum;
import com.jal.crawler.context.ResolveContext;
import com.jal.crawler.rpc.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;


@SpringBootApplication
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class})
public class ResolveApplication {

    public static void main(String[] args) throws IOException {
        Integer type = Integer.valueOf(args[0]);
        Integer port = Integer.valueOf(args[1]);
        String host=args[2];

        ConfigurableApplicationContext run = SpringApplication.run(ResolveApplication.class, args);

        Server server = ServerBuilder
                .forPort(port)
                .addService(run.getBean(ResolveInitServer.class))
                .addService(run.getBean(ResolveTaskServer.class))
                .addService(run.getBean(ResolveStatusServer.class))
                .addService(run.getBean(ResolveLeaderServer.class))
                .addService(run.getBean(ResolveHeartServer.class))
                .build();

        ComponentRelation self = new ComponentRelation();

        self.setHost(host);
        self.setStatus(StatusEnum.NO_INIT);
        self.setRelationTypeEnum(ComponentRelationTypeEnum.numberOf(type));
        self.setPort(port);
        self.setComponentType(1);

        ResolveContext loadContext = run.getBean(ResolveContext.class);

        loadContext.componentStart(self, type==0?self:null);

        server.start();
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
