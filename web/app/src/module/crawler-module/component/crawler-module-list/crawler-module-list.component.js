/**
 * Created by jianganlan on 2017/5/11.
 */
import moduleListController from 'src/module/crawler-module/component/crawler-module-list/crawler-module-list.component.controller';



let name='moduleList'

let config={
    templateUrl:'src/module/crawler-module/component/crawler-module-list/crawler-module-list.component.template.html',
    controller:moduleListController.controller
}

export  default {
    name:name,
    config:config
}