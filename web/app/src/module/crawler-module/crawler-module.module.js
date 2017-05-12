/**
 * Created by jianganlan on 2017/5/11.
 */

import moduleViewComponent from 'src/module/crawler-module/component/crawler-module-view/crawler-module-view.component';
import moduleInfoComponent from 'src/module/crawler-module/component/crawler-module-info/crawler-module-info.component';
import moduleListComponent from 'src/module/crawler-module/component/crawler-module-list/crawler-module-list.component';

import moduleService from 'src/module/crawler-module/service/crawler-module.service';

export default
angular
    .module('crawlerModule', ['ui.router'])
    .config(['$stateProvider', function ($stateProvider) {
        var states = [
            // {
            //     name: 'modulePush',
            //     parent: 'moduleView',
            //     url: '/modulePush',
            //     template: '<module-push></module-push>'
            // },
            {
                name: 'moduleInfo',
                parent: 'moduleView',
                url: '/moduleInfo',
                template: '<module-info></module-info>'
            },
            {
                name: 'moduleList',
                parent: 'moduleView',
                url: '/moduleList',
                template: '<module-list></module-list>'
            }
        ]

        states.forEach(function (state) {
            $stateProvider.state(state);
        })
    }])
    .component(moduleViewComponent.name,moduleViewComponent.config)
    .component(moduleInfoComponent.name,moduleInfoComponent.config)
    .component(moduleListComponent.name,moduleListComponent.config)
    .service(moduleService.name,moduleService.service)

