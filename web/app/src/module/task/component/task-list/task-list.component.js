/**
 * Created by jal on 2017/3/19.
 */
import taskListController from 'src/module/task/component/task-list/task-list.component.controller'

let name = 'taskList';

let config = {
    templateUrl: 'src/module/task/component/task-list/task-list.component.template.html',
    controller:taskListController.controller
}

export default {
    name: name,
    config: config
}