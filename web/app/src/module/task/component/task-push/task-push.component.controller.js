/**
 * Created by jal on 2017/3/19.
 */

let name = 'taskPushController'

let controller = ['taskService','$mdDialog', function (taskService,$mdDialog) {

    var self = this;
    self.urls = [];
    self.processors = [];
    self.resolveData = [];
    self.test=false;

    self.processorType = taskService.processorType;
    self.resolveOptionType = taskService.resolveOptionType;

    self.tempUrl = "";
    self.tempProcessor = {};
    self.tempResolve = {};
    self.taskTag='';

    self.testState={
        notWait:true,
        datas:[]
    };


    self.addLink = function () {
        if (!self.urls.includes(self.tempUrl)) {
            self.urls.push(self.tempUrl);
            self.tempUrl="";
            console.log(self.urls)
        }
    }

    self.addProcessor = function () {
        var index = self.processors.length;
        self.tempProcessor.order = index;
        self.processors.push(self.tempProcessor)
        self.tempProcessor = null;

    }
    self.addResolve = function () {
        self.resolveData.push(self.tempResolve);
        self.tempResolve=null;
    }


    self.findPre = function () {
        return self.processors.filter(function (t) {
            return t.kind == 0;
        })
    }

    self.findPost = function () {
        return self.processors.filter(function (t) {
            return t.kind == 1;
        })
    }
    self.submit  = function(ev) {
       $mdDialog.show({
                contentElement: '#task-push-dialog',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true
              });

    }

    self.clear=function(ev){
        // Appending dialog to document.body to cover sidenav in docs app
        var confirm = $mdDialog.confirm()
              .title('确认是否清空')
              .textContent('一旦你清空了设置，你之前的配置全都没有了哦！')
              .ariaLabel('Lucky day')
              .clickOutsideToClose(true)
              .targetEvent(ev)
              .ok('确认清空')
              .cancel('手滑点错');

        $mdDialog.show(confirm).then(function() {
              self.urls = [];
              self.processors = [];
              self.resolveData = [];
              self.tempUrl = "http://www.baidu.com";
              self.tempProcessor = {};
              self.tempResolve = {};
        }, function() {

        });

    }

    self.taskTest=function(ev){

        $mdDialog.show({
                    contentElement: '#task-test-dialog',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: true
                  });
        self.test=true;
        taskService.taskPush(null,
            {
                download: {
                    dynamic: true,
                    test:self.test,
                    urls: self.urls,
                    preProcess: self.findPre(),
                    postProcess: self.findPost(),
                },
                resolve: {
                    test:self.test,
                    vars: self.resolveData,
                    items: []
                }
            }
        ).then(function (result) {
            self.taskTag=result.data.data.taskTag;
            taskService.taskResult({
                taskTag:self.taskTag,
                dataType:3
            },null).then(function (result) {
                console.log(result);
            })
        });

    }

    self.pushRequest=function(ev){
        taskService.taskPush(null,
                    {
                        download: {
                            dynamic: true,
                            test:self.test,
                            urls: self.urls,
                            preProcess: self.findPre(),
                            postProcess: self.findPost(),
                        },
                        resolve: {
                            test:self.test,
                            vars: self.resolveData,
                            items: []
                        }
                    }
                ).then(function (result) {
                    self.taskTag=result.data.data.taskTag;
                });
    }


}]

export default {
    name: name,
    controller: controller
}