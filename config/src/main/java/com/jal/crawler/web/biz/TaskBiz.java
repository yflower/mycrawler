package com.jal.crawler.web.biz;

import com.jal.crawler.context.ConfigContext;
import com.jal.crawler.web.convert.TaskOperationConvert;
import com.jal.crawler.web.data.enums.TaskOperationEnum;
import com.jal.crawler.web.data.model.task.DownloadOperationModel;
import com.jal.crawler.web.data.model.task.ResolveOperationModel;
import com.jal.crawler.web.data.param.TaskPushParam;
import com.jal.crawler.web.data.view.task.TaskOperationVO;
import com.jal.crawler.web.data.view.task.TaskStatusVO;
import com.jal.crawler.web.service.ITaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;
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


    public TaskOperationVO taskPush(TaskPushParam param) {
        taskOpParamCheck(param);
        String taskTag = generateTaskTag();
        taskPush(param.getDownload(), taskTag);
        taskPush(param.getResolve(), taskTag);
        LOGGER.info("任务添加成功");
        return new TaskOperationVO() {{
            setResult(1);
        }};
    }


    public TaskOperationVO taskPause(String taskTag) {
        ResolveOperationModel resolveOperationModel = new ResolveOperationModel(taskTag);
        resolveOperationModel.setType(TaskOperationEnum.STOP);
        resolveTaskService.pause(resolveOperationModel);

        DownloadOperationModel downloadOperationModel = new DownloadOperationModel();
        downloadOperationModel.setType(TaskOperationEnum.STOP);
        downloadTaskService.pause(downloadOperationModel);

        return new TaskOperationVO() {{
            setResult(1);
        }};
    }

    public TaskOperationVO taskStop(String taskTag) {
        ResolveOperationModel resolveOperationModel = new ResolveOperationModel(taskTag);
        resolveOperationModel.setType(TaskOperationEnum.FINISH);
        resolveTaskService.stop(resolveOperationModel);

        DownloadOperationModel downloadOperationModel = new DownloadOperationModel();
        downloadOperationModel.setType(TaskOperationEnum.FINISH);
        downloadTaskService.stop(downloadOperationModel);

        return new TaskOperationVO() {{
            setResult(1);
        }};
    }

    public TaskOperationVO taskDestroy(String taskTag) {
        ResolveOperationModel resolveOperationModel = new ResolveOperationModel(taskTag);
        resolveOperationModel.setType(TaskOperationEnum.DESTROY);
        resolveTaskService.destroy(resolveOperationModel);

        DownloadOperationModel downloadOperationModel = new DownloadOperationModel();
        downloadOperationModel.setType(TaskOperationEnum.DESTROY);
        downloadTaskService.destroy(downloadOperationModel);

        return new TaskOperationVO() {{
            setResult(1);
        }};
    }

    public TaskStatusVO status(String taskTag){
        configContext.resolveComponent().forEach(t->configContext.getRpcClient().getClient(t));
    }















    private String generateTaskTag() {
        return UUID.randomUUID().toString();
    }

    private void taskOpParamCheck(TaskPushParam param) {
    }

    private void taskPush(TaskPushParam.resolve param, String taskTag) {
        ResolveOperationModel resolveOperationModel = TaskOperationConvert.paramToModel(param, taskTag);
        fillTaskOpModel(resolveOperationModel);
        resolveOperationModel.setType(TaskOperationEnum.ADD);
        resolveTaskService.push(resolveOperationModel);
    }

    private void taskPush(TaskPushParam.download param, String taskTag) {
        DownloadOperationModel downloadOperationModel = TaskOperationConvert.paramToModel(param, taskTag);
        fillTaskOpModel(downloadOperationModel);
        downloadOperationModel.setType(TaskOperationEnum.ADD);
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
