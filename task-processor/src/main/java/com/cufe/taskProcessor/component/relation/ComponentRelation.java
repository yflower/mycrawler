package com.cufe.taskProcessor.component.relation;

import com.cufe.taskProcessor.task.StatusEnum;

/**
 * Created by jianganlan on 2017/4/13.
 */
public class ComponentRelation {
    private int componentType;

    private ComponentRelationTypeEnum relationTypeEnum;

    private ComponentRelation leader;

    private String host;

    //rpc port
    private int port;

    //http port
    private int serverPort;

    private StatusEnum status = StatusEnum.NO_INIT;


    public ComponentRelationTypeEnum getRelationTypeEnum() {
        return relationTypeEnum;
    }

    public void setRelationTypeEnum(ComponentRelationTypeEnum relationTypeEnum) {
        this.relationTypeEnum = relationTypeEnum;
    }

    public ComponentRelation getLeader() {
        if (relationTypeEnum == ComponentRelationTypeEnum.LEADER) {
            return null;
        }
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


    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String address() {
        return host + ":" + port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getComponentType() {
        return componentType;
    }

    public void setComponentType(int componentType) {
        this.componentType = componentType;
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
                ", host='" + host + '\'' +
                ", port=" + port +
                ", serverPort=" + serverPort +
                ", status=" + status +
                '}';
    }
}
