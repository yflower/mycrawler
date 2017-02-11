package com.jal.crawler.web.service;

import com.jal.crawler.proto.Clients;
import com.jal.crawler.proto.ResolveClient;
import com.jal.crawler.web.data.VO.ResolveVO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jal on 2017/2/8.
 */
@Component
public class ResolveService {
    public ResolveVO status(String address) {
        ResolveVO resolveStatus = new ResolveVO();
        if (Clients.resolves.containsKey(address)) {
            ResolveClient client = Clients.resolves.get(address);
            resolveStatus.setStatus(client.status());
            resolveStatus.setAddress(client.address());
            resolveStatus.setThread(client.showConfig().getThread());
            resolveStatus.setTaskNum(client.showTask().size());
            resolveStatus.setTasks(client.showTask());
        }
        return resolveStatus;
    }

    public List<ResolveVO> status() {
        return Clients.resolves.keySet().stream().map(this::status).collect(Collectors.toList());
    }
}
