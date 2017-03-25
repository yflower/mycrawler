/**
 * Created by jal on 2017/3/5.
 */
import angular from 'angular';
import appModule from 'src/app.module';


angular
    .element(document)
    .ready(function () {
        angular
            .module('app start', [appModule.name])
        let body = document.getElementsByTagName('body')[0];
        angular.bootstrap(body, ['app start']);

    })




