/**
 * Created by jal on 2017/3/19.
 */

let name = 'taskPushController'

let controller = ['taskService', function (taskService) {

    var self = this;
    self.urls = [];
    self.processors = [];
    self.resolveData = [];

    self.processorType = taskService.processorType;
    self.resolveOptionType = taskService.resolveOptionType;

    self.tempUrl = "http://www.baidu.com";
    self.tempProcessor = {};
    self.tempResolve = {};

    self.addLink = function () {
        if (!self.urls.includes(self.tempUrl)) {
            self.urls.push(self.tempUrl);
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
    self.submit = function () {
        taskService.taskPush(null,
            {
                download: {
                    dynamic: true,
                    urls: self.urls,
                    preProcess: self.findPre(),
                    postProcess: self.findPost(),
                },
                resolve: {
                    vars: self.resolveData,
                    items: []
                }
            }
        ).then(function (result) {
            console.log(result)
        });
    }


}]

export default {
    name: name,
    controller: controller
}