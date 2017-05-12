/**
 * Created by jianganlan on 2017/5/11.
 */

import moduleInfoController from 'src/module/crawler-module/component/crawler-module-info/crawler-module-info.component.controller';

let name='moduleInfo'

let config={
    templateUrl:'src/module/crawler-module/component/crawler-module-info/crawler-module-info.component.template.html',
    controller:moduleInfoController.controller

}

export  default {
    name:name,
    config:config
}