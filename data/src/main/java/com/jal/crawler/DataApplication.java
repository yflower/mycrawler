package com.jal.crawler;

import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.cufe.taskProcessor.task.StatusEnum;
import com.cufe.taskRpc.RpcUtils;
import com.jal.crawler.context.DataContext;
import com.jal.crawler.enums.ComponentTypeEnum;
import com.jal.crawler.rpc.DataInitServer;
import com.jal.crawler.rpc.DataTaskServer;
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
public class DataApplication {
    public static void main(String[] args) throws IOException, InterruptedException {
        Integer type = Integer.valueOf(args[0]);
        Integer port = Integer.valueOf(args[1]);
        String host = args[2];

        ConfigurableApplicationContext context = SpringApplication.run(DataApplication.class, args);

        DataContext loadContext = context.getBean(DataContext.class);

        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(port)
                .addService(context.getBean(DataInitServer.class))
                .addService(context.getBean(DataTaskServer.class));

        Server server = RpcUtils.registServer(serverBuilder, loadContext).build();

        ComponentRelation self = new ComponentRelation();

        self.setHost(host);
        self.setStatus(StatusEnum.NO_INIT);
        self.setRelationTypeEnum(ComponentRelationTypeEnum.numberOf(type));
        self.setPort(port);
        self.setComponentType(loadContext.componentType());


        loadContext.componentStart(self, type == 0 ? self : null);


        server.start();
        server.awaitTermination();
    }
}
