package com.cufe.taskProcessor;

import com.cufe.taskProcessor.component.client.AbstractComponentClientFactory;
import com.cufe.taskProcessor.component.client.ComponentClient;
import com.cufe.taskProcessor.component.client.ComponentClientHolder;
import com.cufe.taskProcessor.component.relation.ComponentRelation;
import com.cufe.taskProcessor.component.relation.ComponentRelationHolder;
import com.cufe.taskProcessor.component.relation.ComponentRelationTypeEnum;
import com.cufe.taskProcessor.component.status.ComponentStatus;
import com.cufe.taskProcessor.context.ComponentContext;
import com.cufe.taskProcessor.task.AbstractTask;
import com.cufe.taskProcessor.task.StatusEnum;
import com.cufe.taskProcessor.task.TaskTypeEnum;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 组件暴露给调度平台的接口
 * 注意只有leader组件会收到信息
 * 若cluster收到说明leader组件出异常
 * Created by jianganlan on 2017/4/15.
 */
public abstract class ComponentFacade<CONFIG_PARAM extends ComponentFacade.initParam, TASK_OP_PARAM extends ComponentFacade.taskOpParam> {

    private final static Logger LOGGER = Logger.getLogger(ComponentFacade.class.getSimpleName());


    protected ComponentContext componentContext;


    //组件列表
    public List<ComponentRelation> componentList() {

        if (componentContext.getStatus() == StatusEnum.NO_INIT) {
            LOGGER.warning("组件还没有初始化，无法获取列表 ");
            return null;
        }

        ComponentRelation componentRelation = componentContext.getComponentRelation();
        //当前组件为leader组件
        if (componentRelation.getRelationTypeEnum() == ComponentRelationTypeEnum.LEADER) {
            ComponentRelationHolder relationHolder = componentContext.getComponentRelationHolder();
            return relationHolder.connectComponent();
        } else {
            Optional<ComponentRelation> optional = seekLeader();
            //http请求转发到leader
            if (optional.isPresent()) {
                return componentListForward(optional.get());
            }
            LOGGER.log(Level.SEVERE, "无法获取到leader，组件列表获取失败");

            return new ArrayList<>();

        }

    }

    public void componentInit(CONFIG_PARAM configParam) {
        ComponentRelation self = componentContext.getComponentRelation();
        ComponentRelationHolder componentRelationHolder = componentContext.getComponentRelationHolder();
        AbstractComponentClientFactory componentClientFactory = componentContext.getComponentClientFactory();
        ComponentClientHolder componentClientHolder = componentContext.getComponentClientHolder();


        //创建componentRelation
        ComponentRelation componentRelation = new ComponentRelation();
        componentRelation.setHost(configParam.getHost());
        componentRelation.setPort(configParam.getPort());
        Optional<ComponentClient> componentClient;
        ComponentRelation leader = null;


        //只有leader组件才对添加的组件的进行设置
        if (self.getRelationTypeEnum() == ComponentRelationTypeEnum.LEADER) {
            if (!componentRelationHolder.contains(componentRelation)) {
                leader = componentContext.getComponentRelation();
            }
            LOGGER.warning("组件已经被添加");
            return;
        } else {
            Optional<ComponentRelation> optional = seekLeader();
            //http请求转发到leader
            if (optional.isPresent()) {
                leader = optional.get().getLeader();
            }

            LOGGER.log(Level.SEVERE, "无法获取到leader，组件设置失败");

        }


        //组件添加
        componentRelation.setLeader(leader);
        configParam.setLeaderHost(leader.getHost());
        configParam.setLeaderPort(leader.getPort());
        configParam.setSelfStatus(StatusEnum.INIT.getCode());
        configParam.setLeaderStatus(leader.getStatus().getCode());

        componentClient = componentClientFactory.create(componentRelation);
        if (componentClient.isPresent()) {
            //client添加
            componentClientHolder.add(componentRelation, componentClient.get());
            //组件初始化
            componentClient.get().initClient.init(configParam);

            LOGGER.info("添加组件成功 " + componentRelation);

        } else {
            LOGGER.warning("添加组件失败 " + componentRelation);
        }


    }

    public void componentTask(TASK_OP_PARAM taskOpParam) {
        ComponentRelation self = componentContext.getComponentRelation();
        AbstractComponentClientFactory componentClientFactory = componentContext.getComponentClientFactory();

        ComponentClientHolder componentClientHolder = componentContext.getComponentClientHolder();

        //leader为每个组件分配组件
        componentList().forEach(t -> {
            Optional<ComponentClient> optional = componentClientHolder.from(t);
            ComponentClient componentClient = null;
            //获取连接client
            if (optional.isPresent()) {
                componentClient = optional.get();

            } else {
                Optional<ComponentClient> clientOptional = componentClientFactory.create(t);

                if (clientOptional.isPresent()) {
                    componentClientHolder.add(t, clientOptional.get());
                    componentClient = clientOptional.get();

                }

            }
            componentClient.taskClient.task(taskOpParam.getTaskTag()
                    , TaskTypeEnum.numberOf(taskOpParam.getTaskType())
                    , taskOpParam);
        });
    }


    //任务详细汇总
    public List<AbstractTask> taskStatus() {
        List<ComponentRelation> componentRelations = componentList();
        ComponentClientHolder componentClientHolder = componentContext.getComponentClientHolder();

        Map<String, AbstractTask> result = new HashMap<>();

        Map<ComponentRelation, List<AbstractTask>> componentTasks = new HashMap<>();

        componentRelations.forEach(t -> {
            Optional<ComponentClient> client = componentClientHolder.from(t);

            if (client.isPresent()) {
                ComponentClient componentClient = client.get();
                ComponentStatus componentStatus = componentClient.statusClient.status();
                List<AbstractTask> tasks = componentStatus.getTasks();
                componentTasks.put(t, tasks);
            }

        });

        componentTasks.forEach((k, v) -> {
            v.stream().filter(t -> !t.isTest()).forEach(t -> {
                if (result.containsKey(t.getTaskTag())) {
                    AbstractTask task = result.get(t.getTaskTag());
                    task.getTaskStatistics().getResourceFountCycle().accumulate(t.getTaskStatistics().getResourceFountCycle().longValue());
                    task.getTaskStatistics().getResourceNotFoundCycle().accumulate(t.getTaskStatistics().getResourceNotFoundCycle().longValue());
                    task.getTaskStatistics().getPersistSuccessCycle().accumulate(t.getTaskStatistics().getPersistSuccessCycle().longValue());
                    task.getTaskStatistics().getPersistErrorCycle().accumulate(t.getTaskStatistics().getPersistErrorCycle().longValue());
                    task.getTaskStatistics().getProcessorSuccessCycle().accumulate(t.getTaskStatistics().getProcessorSuccessCycle().longValue());
                    task.getTaskStatistics().getProcessorErrorCycle().accumulate(t.getTaskStatistics().getProcessorErrorCycle().longValue());
                } else {
                    result.put(t.getTaskTag(), t);
                }
            });
        });

        return new ArrayList<>(result.values());

    }


    private Optional<ComponentRelation> seekLeader() {
        ComponentRelation leader = componentContext.getComponentRelation().getLeader();

        if (leader == null) {
            //休眠等待leader设置该组件
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (leader != null) {
                return Optional.of(leader);
            }
        }
        return Optional.of(leader);

    }


    protected abstract List<ComponentRelation> componentListForward(ComponentRelation leader);


    public static class initParam {
        private String host;

        private int port;

        private int relationType;

        private int thread;

        private String leaderHost;

        private int leaderPort;

        private int leaderStatus;

        private int selfStatus;

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

        public int getThread() {
            return thread;
        }

        public void setThread(int thread) {
            this.thread = thread;
        }

        public String getLeaderHost() {
            return leaderHost;
        }

        public void setLeaderHost(String leaderHost) {
            this.leaderHost = leaderHost;
        }

        public int getLeaderPort() {
            return leaderPort;
        }

        public void setLeaderPort(int leaderPort) {
            this.leaderPort = leaderPort;
        }

        public int getRelationType() {
            return relationType;
        }

        public void setRelationType(int relationType) {
            this.relationType = relationType;
        }

        public int getLeaderStatus() {
            return leaderStatus;
        }

        public void setLeaderStatus(int leaderStatus) {
            this.leaderStatus = leaderStatus;
        }

        public int getSelfStatus() {
            return selfStatus;
        }

        public void setSelfStatus(int selfStatus) {
            this.selfStatus = selfStatus;
        }
    }

    public static class taskOpParam {
        private String taskTag;

        private int taskType;

        public String getTaskTag() {
            return taskTag;
        }

        public void setTaskTag(String taskTag) {
            this.taskTag = taskTag;
        }

        public int getTaskType() {
            return taskType;
        }

        public void setTaskType(int taskType) {
            this.taskType = taskType;
        }
    }

}
