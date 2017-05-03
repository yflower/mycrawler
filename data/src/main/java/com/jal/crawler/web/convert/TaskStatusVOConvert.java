package com.jal.crawler.web.convert;

import com.cufe.taskProcessor.task.AbstractTask;
import com.jal.crawler.web.param.TaskStatusVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/5/3.
 */
public class TaskStatusVOConvert {
    public static TaskStatusVO convert(AbstractTask abstractTask) {
        TaskStatusVO taskStatusVO = new TaskStatusVO();
        taskStatusVO.setStatus(abstractTask.getStatus());
        taskStatusVO.setTaskTag(abstractTask.getTaskTag());
        taskStatusVO.setTest(false);
        TaskStatusVO.TaskStatistics taskStatistics = new TaskStatusVO.TaskStatistics();
        taskStatistics.setBeginTime(abstractTask.getTaskStatistics().getBeginTime());
        taskStatistics.setEndTime(abstractTask.getTaskStatistics().getEndTime());
        taskStatistics.setCyclePerTime(abstractTask.getTaskStatistics().getCyclePerTime().intValue());
        taskStatistics.setHistoryStatus(abstractTask.getTaskStatistics().getHistoryStatus());
        taskStatistics.setPersistErrorCycle(abstractTask.getTaskStatistics().getPersistErrorCycle().intValue());
        taskStatistics.setPersistSuccessCycle(abstractTask.getTaskStatistics().getPersistSuccessCycle().intValue());
        taskStatistics.setPersistTotalCycle(abstractTask.getTaskStatistics().getPersistTotalCycle());
        taskStatistics.setProcessorErrorCycle(abstractTask.getTaskStatistics().getProcessorErrorCycle().intValue());
        taskStatistics.setProcessorSuccessCycle(abstractTask.getTaskStatistics().getProcessorSuccessCycle().intValue());
        taskStatistics.setProcessorTotalCycle(abstractTask.getTaskStatistics().getProcessorTotalCycle());
        taskStatistics.setResourceNotFoundCycle(abstractTask.getTaskStatistics().getResourceNotFoundCycle().intValue());
        taskStatistics.setResourceFountCycle(abstractTask.getTaskStatistics().getResourceFountCycle().intValue());
        taskStatistics.setResourceTotalCycle(abstractTask.getTaskStatistics().getResourceTotalCycle());
        taskStatusVO.setTaskStatistics(taskStatistics);
        return taskStatusVO;
    }

    public static List<TaskStatusVO> convert(List<AbstractTask> tasks) {
        return tasks.parallelStream().map(TaskStatusVOConvert::convert).collect(Collectors.toList());
    }
}
