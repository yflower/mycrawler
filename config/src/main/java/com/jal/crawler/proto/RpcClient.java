package com.jal.crawler.proto;

import com.google.common.util.concurrent.ListenableFuture;
import com.jal.crawler.proto.configComponnet.ConfigComponentStatus;
import com.jal.crawler.proto.status.ComponentStatus;
import com.jal.crawler.proto.status.RpcComponentStatusGrpc;
import com.jal.crawler.web.convert.RpcEnumConvert;
import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.model.component.ComponentModel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by jal on 2017/1/28.
 */
@Component
public class RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private static Map<String, AbstractComponentClient> clients = new HashMap<>();

    private static Map<String, ResolveClient> resolves = new HashMap<>();

    private static Map<String, DownloadClient> downs = new HashMap<>();

    /**
     * 尝试连接组件，并获取组件类型
     *
     * @param componentModel
     * @return
     */
    public static Optional<ComponentEnum> tryConnect(ComponentModel componentModel) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(componentModel.getHost(), componentModel.getPort())
                .usePlaintext(true)
                .build();
        RpcComponentStatusGrpc.RpcComponentStatusFutureStub futureStub = RpcComponentStatusGrpc.newFutureStub(channel);
        ListenableFuture<ComponentStatus> future = futureStub.rpcComponentStatus(ConfigComponentStatus.getDefaultInstance());

        try {
            ComponentStatus componentStatus = future.get(2, TimeUnit.SECONDS);
            ComponentEnum componentEnum = RpcEnumConvert.componentType(componentStatus.getComponentType());
            componentModel.setComponentEnum(componentEnum);
            return Optional.of(componentEnum);
        } catch (InterruptedException e) {
            return Optional.empty();
        } catch (ExecutionException e) {
            return Optional.empty();
        } catch (TimeoutException e) {
            return Optional.empty();

        }

    }

    public Optional<AbstractComponentClient> getClient(ComponentModel componentModel) {
        String address = componentModel.getHost() + ":" + componentModel.getPort();
        if (clients.containsKey(address)) {
            AbstractComponentClient client = clients.get(address);
            if (client.status().isPresent()) {
                return Optional.of(client);
            } else {
                remove(address);
            }

        }
        return cacheClient(componentModel);
    }

    public List<ComponentModel> resolveModel() {
        Function<AbstractComponentClient, Optional<ComponentModel>> function = t -> {
            Optional<ComponentEnum> componentEnum = t.status();
            if (componentEnum.isPresent()) {
                ComponentModel componentModel = new ComponentModel();
                componentModel.setComponentEnum(ComponentEnum.RESOLVE);
                String address = t.address;
                componentModel.setHost(address.split(":")[0]);
                componentModel.setPort(Integer.parseInt(address.split(":")[1]));
                return Optional.of(componentModel);
            }
            remove(t);
            return Optional.empty();
        };

        return resolves.values().stream()
                .map(function)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<ComponentModel> downloadModel() {
        Function<AbstractComponentClient, Optional<ComponentModel>> function = t -> {
            Optional<ComponentEnum> componentEnum = t.status();
            if (componentEnum.isPresent()) {
                ComponentModel componentModel = new ComponentModel();
                componentModel.setComponentEnum(ComponentEnum.DOWNLOAD);
                String address = t.address;
                componentModel.setHost(address.split(":")[0]);
                componentModel.setPort(Integer.parseInt(address.split(":")[1]));
                return Optional.of(componentModel);
            }
            remove(t);
            return Optional.empty();
        };
        return downs.values().stream()
                .map(function)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<AbstractComponentClient> cacheClient(ComponentModel componentModel) {
        Optional<ComponentEnum> componentEnum = tryConnect(componentModel);
        AbstractComponentClient client;
        if (componentEnum.isPresent()) {
            switch (componentEnum.get()) {
                case DOWNLOAD:
                    client = downloadClient(componentModel.getHost(), componentModel.getPort());
                    break;
                case RESOLVE:
                    client = resolveClient(componentModel.getHost(), componentModel.getPort());
                    break;
                default:
                    return Optional.empty();
            }
            return Optional.of(client);
        }
        return Optional.empty();


    }

    private AbstractComponentClient downloadClient(String host, int port) {
        String address = host + ":" + port;
        DownloadClient downloadClient = new DownloadClient();
        downloadClient.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
        downloadClient.address = address;
        downAdd(downloadClient);
        logger.info("add a downClient");
        return downloadClient;

    }

    private AbstractComponentClient resolveClient(String host, int port) {
        String address = host + ":" + port;
        ResolveClient resolveClient = new ResolveClient();
        resolveClient.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
        resolveClient.address = address;
        resolveAdd(resolveClient);
        logger.info("add a resolveClient");
        return resolveClient;
    }

    private void resolveAdd(ResolveClient resolveClient) {
        resolves.put(resolveClient.address, resolveClient);
        clients.put(resolveClient.address, resolveClient);
    }

    private void downAdd(DownloadClient downloadClient) {
        downs.put(downloadClient.address, downloadClient);
        clients.put(downloadClient.address, downloadClient);
    }

    private void remove(String address) {
        clients.get(address).close();
        clients.remove(address);
        downs.remove(address);
        resolves.remove(address);
    }

    private void remove(AbstractComponentClient client){
        client.close();
        clients.remove(client.address);
        downs.remove(client.address);
        resolves.remove(client.address);
    }

}
