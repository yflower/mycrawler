/**
 * Created by jal on 2017/3/5.
 */


import navService from "src/module/nav/service/nav.service";
import navListComponent from "src/module/nav/component/nav-list/nav-list.component";
import navController from "src/module/nav/nav.controller";

import taskModule from "src/module/task/task.module";

export default
angular
    .module('leftNav', ['ui.router', taskModule.name])
    .config(['$stateProvider', function ($stateProvider) {
        var states = [
            {
                name: "taskView",
                views:{
                    "v1":{
                        name:'view',
                        template:'<task-view></task-view>'

                    }
                }

            }
        ]
        states.forEach(function (state) {
            $stateProvider.state(state);
        })
    }])

    .service(navService.name, navService.service)
    .component(navListComponent.name, navListComponent.config)
    .controller(navController.name, navController.controller)