package com.jal.crawler.rpc;

import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/4/26.
 */
@Component
public class HttpClientHolder {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientHolder.class);

    private static Map<String, AbstractHttpClient> clients = new HashMap<>();

    private static Map<String, ResolveHttpClient> resolves = new HashMap<>();

    private static Map<String, DownloadHttpClient> downs = new HashMap<>();

    private static RestTemplate restTemplate = new RestTemplate();

    /**
     * 尝试连接组件，并获取组件类型
     *
     * @param componentRelation
     * @return
     */
    public static Optional<ComponentEnum> tryConnect(ComponentRelation componentRelation) {
        AbstractHttpClient httpClient = new AbstractHttpClient() {

            @Override
            protected OPStatus internalTask(Object taskOperation) throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            protected boolean internalConfigSet(Object config) {
                return false;
            }

            @Override
            protected boolean validConfig(Object config) {
                return false;
            }
        };
        httpClient.setComponentRelation(componentRelation);
        httpClient.setRestTemplate(restTemplate);
        try {
            Optional<ComponentRelation> status = httpClient.status();
            if (status.isPresent()) {
                return Optional.of(ComponentEnum.numberOf(status.get().getComponentType()));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    public Optional<AbstractHttpClient> getClient(ComponentRelation componentRelation) {
        String address = componentRelation.getHost() + ":" + componentRelation.getPort();
        if (clients.containsKey(address)) {
            AbstractHttpClient client = clients.get(address);
            if (client.status().isPresent()) {
                return Optional.of(client);
            } else {
                remove(address);
            }

        }
        return cacheClient(componentRelation);
    }

    public List<ComponentRelation> resolveModel() {
        Function<AbstractHttpClient, Optional<ComponentRelation>> function = t -> {
            Optional<ComponentRelation> status = t.status();
            if (status.isPresent()) {
                return Optional.of(status.get());
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

    public List<ComponentRelation> downloadModel() {
        Function<AbstractHttpClient, Optional<ComponentRelation>> function = t -> {
            Optional<ComponentRelation> status = t.status();
            if (status.isPresent()) {
                return Optional.of(status.get());
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

    private Optional<AbstractHttpClient> cacheClient(ComponentRelation componentRelation) {
        Optional<ComponentEnum> componentEnum = tryConnect(componentRelation);
        AbstractHttpClient client;
        if (componentEnum.isPresent()) {
            switch (componentEnum.get()) {
                case DOWNLOAD:
                    client = downloadClient(componentRelation.getHost(), componentRelation.getPort());
                    break;
                case RESOLVE:
                    client = resolveClient(componentRelation.getHost(), componentRelation.getPort());
                    break;
                default:
                    return Optional.empty();
            }
            return Optional.of(client);
        }
        return Optional.empty();


    }

    private AbstractHttpClient downloadClient(String host, int port) {
        DownloadHttpClient downloadClient = new DownloadHttpClient();
        downloadClient.setRestTemplate(restTemplate);
        downloadClient.setComponentRelation(new ComponentRelation(host, port));
        downAdd(downloadClient);
        logger.info("添加download rpc客户端");
        return downloadClient;

    }

    private AbstractHttpClient resolveClient(String host, int port) {
        ResolveHttpClient resolveClient = new ResolveHttpClient();
        resolveClient.setComponentRelation(new ComponentRelation(host, port));
        resolveClient.setRestTemplate(restTemplate);
        resolveAdd(resolveClient);
        logger.info("添加resolve rpc客户端");
        return resolveClient;
    }

    private void resolveAdd(ResolveHttpClient resolveClient) {
        resolves.put(resolveClient.componentRelation.getHost(), resolveClient);
        clients.put(resolveClient.componentRelation.getHost(), resolveClient);
    }

    private void downAdd(DownloadHttpClient downloadClient) {
        downs.put(downloadClient.componentRelation.getHost(), downloadClient);
        clients.put(downloadClient.componentRelation.getHost(), downloadClient);
    }

    private void remove(String address) {
        clients.get(address).close();
        clients.remove(address);
        downs.remove(address);
        resolves.remove(address);
    }

    private void remove(AbstractHttpClient client) {
        client.close();
        clients.remove(client.componentRelation.getHost());
        downs.remove(client.componentRelation.getHost());
        resolves.remove(client.componentRelation.getHost());
    }


}