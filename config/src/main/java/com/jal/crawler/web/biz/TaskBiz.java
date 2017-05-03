package com.jal.crawler.web.biz;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.web.convert.TaskOperationConvert;
import com.jal.crawler.web.data.enums.ComponentEnum;
import com.jal.crawler.web.data.enums.DataTypeEnum;
import com.jal.crawler.web.data.enums.TaskOperationEnum;
import com.jal.crawler.web.data.model.task.DataOperationModel;
import com.jal.crawler.web.data.model.task.DownloadOperationModel;
import com.jal.crawler.web.data.model.task.ResolveOperationModel;
import com.jal.crawler.web.data.model.task.TaskStatusModel;
import com.jal.crawler.web.data.param.TaskPushParam;
import com.jal.crawler.web.data.view.task.TaskOperationVO;
import com.jal.crawler.web.data.view.task.TaskStatusVO;
import com.jal.crawler.web.service.ITaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by jal on 2017/3/19.
 */
@Component
public class TaskBiz {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskBiz.class);

    @Resource
    private ConfigContext configContext;

    @Resource
    private ITaskService resolveTaskService;

    @Resource
    private ITaskService downloadTaskService;

    @Resource
    private ITaskService dataTaskService;


    public TaskStatusVO status(String taskTag) {
        TaskStatusModel resolveTaskStatus = resolveTaskService.status(taskTag);

        TaskStatusModel downTaskStatus = downloadTaskService.status(taskTag);


        TaskStatusVO taskStatusVO = new TaskStatusVO();

        taskStatusVO.setStatus(downTaskStatus.getStatus().getCode());
        taskStatusVO.setBeginTime(downTaskStatus.getTaskStatistics().getBeginTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        taskStatusVO.setEndTime(downTaskStatus.getTaskStatistics().getEndTime() == null ? 0 :
                downTaskStatus.getTaskStatistics().getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        );

        taskStatusVO.setCurTime(new Date().getTime());
        taskStatusVO.setTaskTag(downTaskStatus.getTaskTag());
        taskStatusVO.setPageTimes((int) resolveTaskStatus.getTaskStatistics().getPersistSuccessCycle());
        taskStatusVO.setErrorTimes((int) (downTaskStatus.getTaskStatistics().getProcessorErrorCycle()
                + downTaskStatus.getTaskStatistics().getPersistErrorCycle() +
                resolveTaskStatus.getTaskStatistics().getProcessorErrorCycle() +
                resolveTaskStatus.getTaskStatistics().getPersistErrorCycle()));

        return taskStatusVO;
    }


    public ResponseEntity taskResult(String taskTag, DataTypeEnum dataType, boolean isTest) {
        DataOperationModel dataOperationModel = new DataOperationModel();
        dataOperationModel.setTest(isTest);
        dataOperationModel.setComponentType(ComponentEnum.DATA);
        dataOperationModel.setTaskTag(taskTag);
        dataOperationModel.setTaskType(TaskOperationEnum.ADD);
        dataOperationModel.setDataTypeEnum(dataType);

        //task组件推送任务
        dataTaskService.push(dataOperationModel);
        //task组件获取结果
        Map<String, Object> param = new HashMap();
        param.put("taskTag", taskTag);
        param.put("type", dataType.getType());
        Optional<ResponseEntity> entityOptional = dataTaskService.result(param);

        if (entityOptional.isPresent()) {
            return entityOptional.get();
        }
        return ResponseEntity.notFound().build();
    }

    public TaskOperationVO taskPush(TaskPushParam param) {
        taskOpParamCheck(param);
        String taskTag = generateTaskTag();
        taskPush(param.getDownload(), taskTag);
        taskPush(param.getResolve(), taskTag);
        LOGGER.info("任务添加成功");
        return new TaskOperationVO() {{
            setTaskTag(taskTag);
        }};
    }


    public TaskOperationVO taskPause(String taskTag) {
        ResolveOperationModel resolveOperationModel = new ResolveOperationModel(taskTag);
        resolveOperationModel.setTaskType(TaskOperationEnum.STOP);
        resolveTaskService.pause(resolveOperationModel);

        DownloadOperationModel downloadOperationModel = new DownloadOperationModel();
        downloadOperationModel.setTaskType(TaskOperationEnum.STOP);
        downloadTaskService.pause(downloadOperationModel);

        return new TaskOperationVO() {{
            setTaskTag(taskTag);
        }};
    }

    public TaskOperationVO taskStop(String taskTag) {
        ResolveOperationModel resolveOperationModel = new ResolveOperationModel(taskTag);
        resolveOperationModel.setTaskType(TaskOperationEnum.FINISH);
        resolveTaskService.stop(resolveOperationModel);

        DownloadOperationModel downloadOperationModel = new DownloadOperationModel();
        downloadOperationModel.setTaskType(TaskOperationEnum.FINISH);
        downloadTaskService.stop(downloadOperationModel);

        return new TaskOperationVO() {{
            setTaskTag(taskTag);
        }};
    }

    public TaskOperationVO taskDestroy(String taskTag) {
        ResolveOperationModel resolveOperationModel = new ResolveOperationModel(taskTag);
        resolveOperationModel.setTaskType(TaskOperationEnum.DESTROY);
        resolveTaskService.destroy(resolveOperationModel);

        DownloadOperationModel downloadOperationModel = new DownloadOperationModel();
        downloadOperationModel.setTaskType(TaskOperationEnum.DESTROY);
        downloadTaskService.destroy(downloadOperationModel);

        return new TaskOperationVO() {{
            setTaskTag(taskTag);
        }};
    }


    private String generateTaskTag() {
        return UUID.randomUUID().toString();
    }

    private void taskOpParamCheck(TaskPushParam param) {
    }

    private void taskPush(TaskPushParam.resolve param, String taskTag) {
        ResolveOperationModel resolveOperationModel = TaskOperationConvert.paramToModel(param, taskTag);
        fillTaskOpModel(resolveOperationModel);
        resolveOperationModel.setTaskType(TaskOperationEnum.ADD);
        resolveTaskService.push(resolveOperationModel);
    }

    private void taskPush(TaskPushParam.download param, String taskTag) {
        DownloadOperationModel downloadOperationModel = TaskOperationConvert.paramToModel(param, taskTag);
        fillTaskOpModel(downloadOperationModel);
        downloadOperationModel.setTaskType(TaskOperationEnum.ADD);
        downloadTaskService.push(downloadOperationModel);
    }


    //rpc中参数不为空，填充可能为空的参数
    private void fillTaskOpModel(ResolveOperationModel model) {
        Consumer<ResolveOperationModel.var> op =
                t -> {
                    if (t.getOption() == null) {
                        t.setOption("");
                    }
                    if (t.getOptionValue() == null) {
                        t.setOptionValue("");
                    }
                };

        model.getVars().forEach(op);
        model.getItems().forEach(k -> k.getVars().forEach(op));
    }

    private void fillTaskOpModel(DownloadOperationModel model) {
        Consumer<DownloadOperationModel.process> op =
                t -> {
                    if (t.getValue() == null) {
                        t.setValue("");
                    }
                };
        model.getPreProcess().forEach(op);
        model.getPostProcess().forEach(op);
    }
    //


}
