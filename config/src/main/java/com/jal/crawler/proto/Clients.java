package com.jal.crawler.proto;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jal on 2017/1/28.
 */
public class Clients {
    private static final Logger logger = LoggerFactory.getLogger(Clients.class);

    public static Map<String, AbstractComponentClient> clients = new HashMap<>();

    public static Map<String, ResolveClient> resolves = new HashMap<>();

    public static Map<String, DownloadClient> downs = new HashMap<>();

    public static AbstractComponentClient downloadClient(String host, int port) {
        String address = host + ":" + port;
        if (clients.containsKey(address)) {
            return clients.get(address);
        } else {
            DownloadClient downloadClient = new DownloadClient();
            downloadClient.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
            downloadClient.address = address;
            downAdd(downloadClient);
            logger.info("add a downClient");
            return downloadClient;
        }
    }

    public static AbstractComponentClient resolveClient(String host, int port) {
        String address = host + ":" + port;
        if (clients.containsKey(address)) {
            return clients.get(address);
        } else {
            ResolveClient resolveClient = new ResolveClient();
            resolveClient.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
            resolveClient.address = address;
            resolveAdd(resolveClient);
            logger.info("add a resolveClient");
            return resolveClient;
        }
    }

    public static void close(AbstractComponentClient abstractComponentClient) {
        if (clients.containsKey(abstractComponentClient.address)) {
            abstractComponentClient.close();
            clients.remove(abstractComponentClient.address);
            resolves.remove(abstractComponentClient.address);
            downs.remove(abstractComponentClient.address);
        }
    }

    private static void resolveAdd(ResolveClient resolveClient) {
        resolves.put(resolveClient.address, resolveClient);
        clients.put(resolveClient.address, resolveClient);
    }

    private static void downAdd(DownloadClient downloadClient) {
        downs.put(downloadClient.address, downloadClient);
        clients.put(downloadClient.address, downloadClient);
    }

}
