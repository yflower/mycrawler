/**
 * Created by jianganlan on 2017/5/4.
 */
let name = 'taskInfoController'

let controller = ['taskService', '$location', function (taskService, $location) {
    var arr = $location.path().split("/");

    var self = this;
    self.taskTag = arr[arr.length - 1];
    self.config = {};
    self.status={};
    self.result={};

    if (self.taskTag != '') {
        configReq();
    }

    function configReq () {
        taskService.taskConfig({
            taskTag: self.taskTag
        }).then(function (result) {
            self.config = result.data;
            console.log(self.config);
        });
        taskService.taskStatus({
            taskTag:self.taskTag
        }).then(function (result) {
            self.status = result.data.data;
            console.log(self.status);
        });
        taskService.taskResult({
            taskTag:self.taskTag,
            dataType:3
        },null).then(function (result) {
            self.result=result.data;
            console.log(self.result);
        },function (error) {
            alert("获取结果出错")
        })

    }


}]

export default {
    name: name,
    controller: controller
}