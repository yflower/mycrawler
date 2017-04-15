package com.cufe.taskProcessor.component.relation;

import com.cufe.taskProcessor.task.StatusEnum;

/**
 * Created by jianganlan on 2017/4/13.
 */
public class ComponentRelation {
    private ComponentRelationTypeEnum relationTypeEnum;

    private ComponentRelation leader;

    private String host;

    private StatusEnum status;

    private int statusPort;

    private int initPort;

    private int taskPort;



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


    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String address(){
        return host;
    }


    public int getStatusPort() {
        return statusPort;
    }

    public void setStatusPort(int statusPort) {
        this.statusPort = statusPort;
    }

    public int getInitPort() {
        return initPort;
    }

    public void setInitPort(int initPort) {
        this.initPort = initPort;
    }

    public int getTaskPort() {
        return taskPort;
    }

    public void setTaskPort(int taskPort) {
        this.taskPort = taskPort;
    }
}
