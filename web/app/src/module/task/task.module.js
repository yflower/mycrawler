/**
 * Created by jal on 2017/3/5.
 */
import taskViewComponent from "src/module/task/component/task-view/task-view.component";
import taskPushComponent from "src/module/task/component/task-push/task-push.component";
import taskInfoComponent from "src/module/task/component/task-info/task-info.component";
import taskListComponent from "src/module/task/component/task-list/task-list.component";


import taskService from "src/module/task/service/task.service";

export default
angular
    .module('taskModule', ['ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        var states = [
            {
                name: 'taskPush',
                parent: 'taskView',
                url: '/taskPush',
                template: '<task-push></task-push>'
            },
            {
                name: 'taskInfo',
                parent: 'taskView',
                url: '/taskInfo/{taskTag}',
                template: '<task-info></task-info>'
            },
            {
                name: 'taskList',
                parent: 'taskView',
                url: '/taskList',
                template: '<task-list></task-list>'
            }
        ]

        states.forEach(function (state) {
            $stateProvider.state(state);
        })
    }])
    .component(taskViewComponent.name, taskViewComponent.config)
    .component(taskPushComponent.name, taskPushComponent.config)
    .component(taskInfoComponent.name, taskInfoComponent.config)
    .component(taskListComponent.name, taskListComponent.config)
    .service(taskService.name, taskService.service)
