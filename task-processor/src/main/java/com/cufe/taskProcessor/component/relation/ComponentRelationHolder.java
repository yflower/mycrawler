package com.cufe.taskProcessor.component.relation;

import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.component.client.ComponentClient;
import com.cufe.taskProcessor.component.client.ComponentClientHolder;
import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.task.StatusEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by jianganlan on 2017/4/13.
 */
public class ComponentRelationHolder {
    private static final Logger LOGGER = Logger.getLogger(ComponentRelationHolder.class.getSimpleName());

    private ComponentContext componentContext;

    private List<ComponentRelation> clusters = new ArrayList<>();

    private List<ComponentRelation> connected = new ArrayList<>();


    private volatile boolean heartCheckStart = false;

    public ComponentRelationHolder(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }

    public boolean addRelation(ComponentRelation componentRelation) {
        ComponentClientHolder componentClientHolder = componentContext.getComponentClientHolder();

        Optional<ComponentClient> clientOptional = componentClientHolder.from(componentRelation);

        ComponentClient componentClient;

        if (!clientOptional.isPresent()) {
            Optional<ComponentClient> optional = componentContext.getComponentClientFactory().create(componentRelation);
            if (optional.isPresent()) {
                componentClient = optional.get();
            } else {
                LOGGER.warning("SERVER:必须添加组件的client，才能加入相关的组件");
                return false;
            }
        } else {
            componentClient = clientOptional.get();
        }

        Optional<StatusEnum> enumOptional = componentClient.tryConnect();
        if (!enumOptional.isPresent()) {
            LOGGER.warning("SERVER:组件尝试连接失败，无法加入组件列表中，" + componentRelation);
            return false;
        }

        boolean result = clusters.add(componentRelation);
        connected.add(componentRelation);

        LOGGER.info("SERVER:成功将组件添加到组件列表 " + componentRelation);

        //开启心跳检测
        componentHeartCheck();
        return result;
    }

    public boolean contains(ComponentRelation componentRelation) {
        return connected.stream()
                .filter(t -> t.getHost().equals(componentRelation.getHost()))
                .filter(t -> t.getPort() == componentRelation.getPort())
                .count() == 1;
    }

    public List<ComponentRelation> connectComponent() {
        return connected;
    }

    private void componentHeartCheck() {
        ComponentClientHolder componentClientHolder = componentContext.getComponentClientHolder();

        AbstractComponentClientFactory componentClientFactory = componentContext.getComponentClientFactory();
        if (!heartCheckStart &&
                componentContext.getComponentRelation().getRelationTypeEnum() == ComponentRelationTypeEnum.LEADER) {
            heartCheckStart = true;
            new Thread(() -> {
                for (; ; ) {
                    List<ComponentRelation> tmpList = new ArrayList<>(connected);
                    tmpList.stream().filter(t -> t.getRelationTypeEnum() == ComponentRelationTypeEnum.SLAVE).forEach(t -> {
                        Optional<ComponentClient> clientOptional = componentClientHolder.from(t);
                        if (clientOptional.isPresent()) {
                            ComponentClient componentClient = clientOptional.get();
                            StatusEnum newStatus;
                            Optional<StatusEnum> enumOptional = componentClient.tryConnect();
                            boolean tryConnect = enumOptional.isPresent();
                            LOGGER.info("心跳检测 result=" + tryConnect);

                            if (!tryConnect) {
                                LOGGER.warning("组件心跳检测失败，尝试重新检测 " + t);

                                boolean retry = false;

                                for (int i = 0; i < 3; ++i) {
                                    if (retry) {
                                        break;
                                    }
                                    retry = componentClient.tryConnect().isPresent();
                                }

                                if (!retry) {
                                    LOGGER.warning("尝试重新连接失败，尝试重新获取连接client" + t);
                                }

                                componentClientHolder.remove(t);

                                LOGGER.warning("开始获取新的连接client " + t);

                                Optional<ComponentClient> optional = componentClientFactory.create(t);

                                LOGGER.warning("");
                                if (!optional.isPresent()) {
                                    LOGGER.warning("获取新的连接client失败，移除组件 " + t);
                                    connected.remove(t);
                                    return;
                                }

                                LOGGER.warning("获取新的连接client成功 " + t);

                            }

                            newStatus = enumOptional.get();
                            if (newStatus != t.getStatus()) {
                                t.setStatus(newStatus);
                            }
                        } else {
                            LOGGER.info("心跳检测失败，移除" + t);
                            componentClientHolder.remove(t);
                            connected.remove(t);
                        }
                    });
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


            }).start();
        }
    }



}
