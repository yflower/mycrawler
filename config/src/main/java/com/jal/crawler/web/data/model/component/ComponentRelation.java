package com.jal.crawler.web.data.model.component;

import com.jal.crawler.web.data.enums.ComponentRelationTypeEnum;
import com.jal.crawler.web.data.enums.StatusEnum;

/**
 * Created by jal on 2017/2/19.
 */
public class ComponentRelation {
    private int componentType;

    private ComponentRelationTypeEnum relationTypeEnum;

    private ComponentRelation leader;

    private String host;

    private int port;

    private int serverPort;

    private StatusEnum status = StatusEnum.NO_INIT;

    public ComponentRelation() {
    }

    public ComponentRelation(String host, int port,int serverPort) {
        this.host = host;
        this.port = port;
        this.serverPort=serverPort;
    }

    public String tag(){
        return host+"_"+port+"_"+serverPort;
    }

    public int getComponentType() {
        return componentType;
    }

    public void setComponentType(int componentType) {
        this.componentType = componentType;
    }

    public ComponentRelationTypeEnum getRelationTypeEnum() {
        return relationTypeEnum;
    }

    public void setRelationTypeEnum(ComponentRelationTypeEnum relationTypeEnum) {
        this.relationTypeEnum = relationTypeEnum;
    }

    public ComponentRelation getLeader() {
        return leader;
    }

    public void setLeader(ComponentRelation leader) {
        this.leader = leader;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public String toString() {
        return "ComponentRelation{" +
                "componentType=" + componentType +
                ", relationTypeEnum=" + relationTypeEnum +
                ", leader=" + leader +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", status=" + status +
                '}';
    }
}
