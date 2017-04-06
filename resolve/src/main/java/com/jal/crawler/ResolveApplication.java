package com.jal.crawler;

import com.jal.crawler.proto.GrpcComponentStatusServer;
import com.jal.crawler.proto.GrpcComponentInitServer;
import com.jal.crawler.proto.GrpcComponentTaskServer;
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
                .addService(run.getBean(GrpcComponentInitServer.class))
                .addService(run.getBean(GrpcComponentTaskServer.class))
                .addService(run.getBean(GrpcComponentStatusServer.class))
                .build();
        server.start();
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
