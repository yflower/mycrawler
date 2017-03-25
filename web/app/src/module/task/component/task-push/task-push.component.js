/**
 * Created by jal on 2017/3/11.
 */
import taskPushController from 'src/module/task/component/task-push/task-push.component.controller';

let name = 'taskPush';

let config = {
    templateUrl: 'src/module/task/component/task-push/task-push.component.template.html',
    controller:taskPushController.controller
}
export default {
    name: name,
    config: config
}