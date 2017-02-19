package com.jal.crawler;

import com.jal.crawler.proto.GrpcComponentServer;
import com.jal.crawler.proto.GrpcConfigServer;
import com.jal.crawler.proto.GrpcTaskServer;
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

        ConfigurableApplicationContext run = SpringApplication.run(ResolveApplication.class, args);

        Server server = ServerBuilder
                .forPort(9006)
                .addService(run.getBean(GrpcConfigServer.class))
                .addService(run.getBean(GrpcTaskServer.class))
                .addService(run.getBean(GrpcComponentServer.class))
                .build();
        server.start();
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
