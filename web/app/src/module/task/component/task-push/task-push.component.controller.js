/**
 * Created by jal on 2017/3/19.
 */

let name = 'taskPushController'

let controller = ['taskService', '$mdDialog', function (taskService, $mdDialog) {

    var self = this;
    //**最终传输的数据**//
    self.urls = [
        "http://wetest.qq.com/bee/Game/109#/alldata"
        // "http://www.cufe.edu.cn/xyxx/zbgg/index.htm"
        // "https://list.jd.com/list.html?cat=670,671,672"

    ];
    self.processors = [
        {
            order:0,
            kind:1,
            type:0,
            query:'#showpage > div.alldata > div > div.allDataPart > ul > li:nth-child(2) > div.conditionDetail.allCateType > ul > li.cateDataType.cateDataType_2',
            option:''
        },
        {
            order:1,
            kind:1,
            type:4,
            query:'#entityTable > table > tbody > tr:nth-child(4) > td.starContent > div',
            option:''
        },
        {
            order:2,
            kind:1,
            type:6,
            query:'',
            option:''
        },{
            order:3,
            kind:1,
            type:0,
            query:'#entityPage > div > a.nextPage.checkPage',
            option:''
        }
        ,{
            order:4,
            kind:1,
            type:5,
            query:1,
            option:''
        }

        ];
    self.resolveData = [
                // {
                //     name: 'link',
                //     query: "#plist > ul > li:nth-child(1) > div > div.p-img > a",
                //     option: "attr",
                //     optionValue: "href"

                // }


    ];
    self.resolveItem = [
        {
        itemName: 'testItem',
        vars: [
            {
                name: 'title',
                query: "#entityTable > table > tbody > tr:nth-child(4) > td.starContent > div",
                option: "",
                optionValue: ""
            }
           , {
                name: 'score',
                query: "#entityTable > table > tbody > tr:nth-child(27) > td:nth-child(3) > img",
                option: "attr",
                optionValue: "href"
            },
            {
                name: 'store',
                query: "#entityTable > table > tbody > tr:nth-child(26) > td:nth-child(4)",
                option: "",
                optionValue: ""
            },
            {
                name: 'time',
                query: "#entityTable > table > tbody > tr:nth-child(25) > td:nth-child(5)",
                option: "",
                optionValue: ""
            }
        ]
        }
    ];
    self.linkPattern = [
        // "https://item.jd.com/11926995.html/.*htm.*"
    ];
    self.test = false;
    //***************//

    self.processorType = taskService.processorType;
    self.resolveOptionType = taskService.resolveOptionType;
    self.tempUrl = "";
    self.tempLinkPattern=""
    self.tempProcessor = {};
    self.tempResolve = {
        dataType: {
            type: 0,
            hide: true,
            label: ''
        }
    };

    self.tempResolveTypeChange = function (type) {
        self.tempResolve.dataType.type = type;

        if (type == 0) {
            self.tempResolve.dataType.hide = true;
            self.tempResolve.dataType.label = '';
        } else {
            self.tempResolve.dataType.hide = false;
            self.tempResolve.dataType.label = '表单名称';
        }
    }


    self.taskTag = '';
    self.testState = {
        notWait: false,
        datas: []
    };


    self.addLink = function () {
        if (!self.urls.includes(self.tempUrl)) {
            self.urls.push(self.tempUrl);
            self.tempUrl = "";
            console.log(self.urls)
        }
    }
    self.addLinkPattern = function () {
        if (!self.linkPattern.includes(self.tempLinkPattern)) {
            self.linkPattern.push(self.tempLinkPattern);
            self.tempLinkPattern = "";
            console.log(self.linkPattern)
        }
    }

    self.addProcessor = function () {
        var index = self.processors.length;
        self.tempProcessor.order = index;
        self.processors.push(self.tempProcessor)
        self.tempProcessor = null;

    }
    self.addResolve = function () {
        console.log(self.tempResolve.dataType.type)
        if (self.tempResolve.dataType.type == 0) {
            //普通类型数据
            self.resolveData.push(self.tempResolve);
            self.tempResolve = null;
        } else if (self.tempResolve.dataType.type == 1) {
            //列表类型数据
            var tmp;
            if (self.resolveItem.find(function (value) {

                    var result = value.itemName == self.tempResolve.dataType.value;
                    if (result) {
                        tmp = value;
                    }
                    return result;
                }) != undefined) {
                tmp.vars.push(self.tempResolve);
                self.tempResolve = {
                    dataType: self.tempResolve.dataType
                };
            }else {
                self.resolveItem.push({
                    itemName:self.tempResolve.dataType.value,
                    vars:[self.tempResolve]
                })
            }
        }
        console.log(self.resolveData)
        console.log(self.resolveItem)

    }


    self.findPre = function () {
        console.log(self.processors)
        return self.processors.filter(function (t) {
            return t.kind == 0;
        })
    }

    self.findPost = function () {
        return self.processors.filter(function (t) {
            return t.kind == 1;
        })
    }
    self.submit = function (ev) {
        if (paramCheck()) {
            alert("配置参数不能为空");
            return;
        }
        $mdDialog.show({
            contentElement: '#task-push-dialog',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: true
        });

    }

    self.clear = function (ev) {
        // Appending dialog to document.body to cover sidenav in docs app
        var confirm = $mdDialog.confirm()
            .title('确认是否清空')
            .textContent('一旦你清空了设置，你之前的配置全都没有了哦！')
            .ariaLabel('Lucky day')
            .clickOutsideToClose(true)
            .targetEvent(ev)
            .ok('确认清空')
            .cancel('手滑点错');

        $mdDialog.show(confirm).then(function () {
            self.urls = [];
            self.processors = [];
            self.resolveData = [];
            self.tempUrl = '';
            self.tempProcessor = {};
            self.tempResolve = {};
        }, function () {

        });

    }

    self.taskTest = function (ev) {
        if (paramCheck()) {
            alert("配置参数不能为空");
            return;
        }
        self.testState.dates = [];
        self.testState.notWait = false;
        $mdDialog.show({
            contentElement: '#task-test-dialog',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false
        });
        self.test = true;
        taskService.taskPush(null,
            {
                download: {
                    dynamic: true,
                    test: self.test,
                    urls: self.urls,
                    preProcess: self.findPre(),
                    postProcess: self.findPost(),
                },
                resolve: {
                    test: self.test,
                    vars: self.resolveData,
                    items: []
                },
                link: {
                    test: self.test,
                    linkPattern: self.linkPattern
                }
            }
        ).then(function (result) {
            self.test = false;
            self.taskTag = result.data.data.taskTag;
            taskService.taskResult({
                taskTag: self.taskTag,
                dataType: 3
            }, null).then(function (result) {
                self.testState.dates = result.data;
                self.testState.notWait = true;

            }, function (error) {
                alert("获取结果出错")
            })


        }, function (error) {
            alert("配置测试出错")
        });

    }

    self.pushRequest = function (ev) {
        taskService.taskPush(null,
            {
                download: {
                    dynamic: true,
                    test: self.test,
                    urls: self.urls,
                    preProcess: self.findPre(),
                    postProcess: self.findPost(),
                },
                resolve: {
                    test: self.test,
                    vars: self.resolveData,
                    items: self.resolveItem
                },
                link: {
                    test: self.test,
                    linkPattern: self.linkPattern
                }
            }
        ).then(function (result) {
            self.taskTag = result.data.data.taskTag;
            $mdDialog.hide();
            var alter = $mdDialog.alert()
                .title('添加成功')
                .textContent('任务添加成功')
                .clickOutsideToClose(true)
                .targetEvent(ev)
                .ok('确认');
            $mdDialog.show(alter)
        });

    }

    function paramCheck() {
        return self.urls.length == 0 || (self.resolveData.length == 0 && self.resolveItem.length == 0);

    }


}]

export default {
    name: name,
    controller: controller
}