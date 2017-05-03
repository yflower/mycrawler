package com.jal.crawler.web.controller;

import com.jal.crawler.web.biz.TaskBiz;
import com.jal.crawler.web.data.apiResponse.ApiResponse;
import com.jal.crawler.web.data.enums.DataTypeEnum;
import com.jal.crawler.web.data.param.TaskPushParam;
import com.jal.crawler.web.data.view.task.TaskStatusVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by jal on 2017/3/19.
 */
@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {

    private static final Logger LOOGER = LoggerFactory.getLogger(TaskController.class);


    @Resource
    private TaskBiz taskBiz;

    /**
     * 添加任务
     *
     * @param param
     * @return
     */
    @PostMapping("/push")
    public ApiResponse push(@RequestBody TaskPushParam param) {
        return ApiResponse.successBuild(taskBiz.taskPush(param));

    }

    @PostMapping("/pause")
    public ApiResponse pause(@RequestBody String taskTag) {
        return ApiResponse.successBuild(taskBiz.taskPause(taskTag));
    }

    @PostMapping("/stop")
    public ApiResponse stop(@RequestBody String taskTag) {
        return ApiResponse.successBuild(taskBiz.taskStop(taskTag));
    }

    @PostMapping("/destroy")
    public ApiResponse destroy(@RequestBody String taskTag) {
        return ApiResponse.successBuild(taskBiz.taskDestroy(taskTag));
    }

    @GetMapping("/status")
    public Object status(String taskTag) {
        if(taskTag!=null){
            Optional<TaskStatusVO> statusVOOptional = taskBiz.status(taskTag);
            if(statusVOOptional.isPresent()){
                return ApiResponse.successBuild(taskBiz.status(taskTag));
            }else {
                return ResponseEntity.notFound().build();
            }
        }else {
            return ApiResponse.successBuild(taskBiz.statusList());
        }

    }



    @GetMapping("/result")
    public ResponseEntity result(String taskTag, int dataType) {
        return taskBiz.taskResult(taskTag, DataTypeEnum.numberOf(dataType), false);
    }
}
