/**
 * Created by jal on 2017/3/5.
 */
import angular from 'angular';
import 'angular-material';
import 'angular-aria';
import 'angular-animate';
import 'angular-ui-router';
import appController from 'src/app.controller';
// import echartService from 'src/service/echart.service';
import appService from 'src/service/app.service';
import navModule from 'src/module/nav/nav.module';

export default
angular.module('app', ['ngMaterial', navModule.name])

    .config(($mdIconProvider, $mdThemingProvider) => {
        // Register the user `avatar` icons
        $mdIconProvider
            .defaultIconSet("./assets/svg/avatars.svg", 128)
            .icon("menu", "./assets/svg/menu.svg", 24)
            .icon("share", "./assets/svg/share.svg", 24)
            .icon("google_plus", "./assets/svg/google_plus.svg", 24)
            .icon("hangouts", "./assets/svg/hangouts.svg", 24)
            .icon("twitter", "./assets/svg/twitter.svg", 24)
            .icon("phone", "./assets/svg/phone.svg", 24)
            .icon("add", "./assets/svg/add.svg", 24)
            .icon("link", "./assets/svg/link.svg", 48)
            .icon("processor", "./assets/svg/processor.svg", 48)
            .icon("up", "./assets/svg/up.svg", 48)
            .icon("down", "./assets/svg/down.svg", 48)
            .icon("edit", "./assets/svg/edit.svg", 48)
            .icon("clear", "./assets/svg/clear.svg", 48)
            .icon("data", "./assets/svg/data.svg", 48)
            .icon("code", "./assets/svg/code.svg", 48)
            .icon("done", "./assets/svg/done.svg", 48)
            .icon("fresh", "./assets/svg/fresh.svg", 48)
            .icon("test", "./assets/svg/test.svg", 48)
            .icon("delete", "./assets/svg/delete.svg", 48)
            .icon("info", "./assets/svg/info.svg", 48)
            .icon("pause", "./assets/svg/pause.svg", 48)
            .icon("start", "./assets/svg/start.svg", 48)
            .icon("stop", "./assets/svg/stop.svg", 48)
            .icon("time", "./assets/svg/time.svg", 48)
            .icon("download", "./assets/svg/download.svg", 48)
            .icon("module", "./assets/svg/module.svg", 48)
            .icon("http", "./assets/svg/http.svg", 48)
            .icon("status", "./assets/svg/status.svg", 48)
            .icon("rpc", "./assets/svg/rpc.svg", 48)
            .icon("task", "./assets/svg/task.svg", 48);

        $mdThemingProvider.theme('default')
            .primaryPalette('indigo')
            .accentPalette('blue');

        $mdThemingProvider.theme("card")
            .primaryPalette('blue')
            .accentPalette('cyan')
    })
    // .service(echartService.name,echartService.service)
    .service(appService.name,appService.service)
    .controller(appController.name, appController.controller)