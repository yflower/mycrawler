package com.jal.crawler.web.service;

import com.jal.crawler.proto.Clients;
import com.jal.crawler.proto.ComponentClient;
import com.jal.crawler.proto.DownloadClient;
import com.jal.crawler.web.data.VO.DownloadVO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jal on 2017/2/10.
 */
@Component
public class DownloadService {
    public List<DownloadVO> status() {
        return Clients.downs.keySet().stream().map(this::status).collect(Collectors.toList());
    }

    public DownloadVO status(String address) {
        DownloadVO downloadVO = new DownloadVO();
        if (Clients.downs.containsKey(address)) {
            DownloadClient client = Clients.downs.get(address);
            downloadVO.setStatus(client.status());
            downloadVO.setAddress(client.address());
            downloadVO.setThread(client.showConfig().getThread());
            downloadVO.setSleepTime(client.showConfig().getSleepTime());
            downloadVO.setProxy(client.showConfig().getProxy());
            downloadVO.setProxyAddress(client.showConfig().getProxyAddressList());
            downloadVO.setTaskNum(client.showTask().size());
            downloadVO.setTasks(client.showTask());
        }
        return downloadVO;
    }
}
