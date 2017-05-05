/**
 * Created by jianganlan on 2017/5/4.
 */

let name = 'taskListController'

let controller = ['taskService', '$mdDialog', function (taskService, $mdDialog) {
    var self = this;

    self.tasks = [];

    self.start = [];

    self.stop = [];

    self.finish = [];

    self.destroy = []

    console.log(this.parent)

    fresh();

    function fresh(){
        taskService.taskStatus().then(function (result) {
            self.tasks = result.data.data;

            self.start = self.tasks.filter(function (value) {
                return value.status == 2;
            })
            self.stop = self.tasks.filter(function (value) {
                return value.status == 3;
            })
            self.finish = self.tasks.filter(function (value) {
                return value.status == 4;
            })
            self.destroy = self.tasks.filter(function (value) {
                return value.status == 5;
            })

            console.log(self)
        })
    }


    self.pauseTask=function ($ev,taskTag) {
        taskService.taskPause({
            taskTag:taskTag
        }).then(function (result) {
            fresh();
        })
    }

    self.stopTask=function ($ev,taskTag) {
        taskService.taskStop({
            taskTag:taskTag
        }).then(function (result) {
            fresh();
        })
    }

    self.destroyTask=function ($ev,taskTag) {
        taskService.taskDestroy({
            taskTag:taskTag
        }).then(function (result) {
            fresh();
        })
    }

    self.restartTask=function ($ev,taskTag) {
        taskService.taskRestart({
            taskTag:taskTag
        }).then(function (result) {
            fresh();
        })
    }

}]

export default {
    name: name,
    controller: controller
}
