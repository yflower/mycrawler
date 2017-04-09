System.config({
  baseURL: "/app",
  defaultJSExtensions: true,
  transpiler: "traceur",
  paths: {
    "github:*": "jspm_packages/github/*",
    "npm:*": "jspm_packages/npm/*"
  },

  map: {
    "angular": "github:angular/bower-angular@1.6.4",
    "angular-animate": "github:angular/bower-angular-animate@1.6.4",
    "angular-aria": "github:angular/bower-angular-aria@1.6.4",
    "angular-material": "github:angular/bower-material@1.1.3",
    "angular-messages": "github:angular/bower-angular-messages@1.6.4",
    "angular-ui-router": "npm:angular-ui-router@0.4.2",
    "css": "github:systemjs/plugin-css@0.1.33",
    "echarts": "npm:echarts@3.5.2",
    "json": "github:systemjs/plugin-json@0.3.0",
    "text": "github:systemjs/plugin-text@0.0.9",
    "traceur": "github:jmcriffey/bower-traceur@0.0.93",
    "traceur-runtime": "github:jmcriffey/bower-traceur-runtime@0.0.93",
    "github:angular/bower-angular-animate@1.6.4": {
      "angular": "github:angular/bower-angular@1.6.4"
    },
    "github:angular/bower-angular-aria@1.6.4": {
      "angular": "github:angular/bower-angular@1.6.4"
    },
    "github:angular/bower-angular-messages@1.6.4": {
      "angular": "github:angular/bower-angular@1.6.4"
    },
    "github:angular/bower-material@1.1.3": {
      "angular": "github:angular/bower-angular@1.6.4",
      "angular-animate": "github:angular/bower-angular-animate@1.6.4",
      "angular-aria": "github:angular/bower-angular-aria@1.6.4",
      "css": "github:systemjs/plugin-css@0.1.33"
    },
    "github:jspm/nodelibs-assert@0.1.0": {
      "assert": "npm:assert@1.4.1"
    },
    "github:jspm/nodelibs-buffer@0.1.0": {
      "buffer": "npm:buffer@3.6.0"
    },
    "github:jspm/nodelibs-process@0.1.2": {
      "process": "npm:process@0.11.9"
    },
    "github:jspm/nodelibs-util@0.1.0": {
      "util": "npm:util@0.10.3"
    },
    "github:jspm/nodelibs-vm@0.1.0": {
      "vm-browserify": "npm:vm-browserify@0.0.4"
    },
    "npm:angular-ui-router@0.4.2": {
      "angular": "npm:angular@1.6.4",
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:assert@1.4.1": {
      "assert": "github:jspm/nodelibs-assert@0.1.0",
      "buffer": "github:jspm/nodelibs-buffer@0.1.0",
      "process": "github:jspm/nodelibs-process@0.1.2",
      "util": "npm:util@0.10.3"
    },
    "npm:buffer@3.6.0": {
      "base64-js": "npm:base64-js@0.0.8",
      "child_process": "github:jspm/nodelibs-child_process@0.1.0",
      "fs": "github:jspm/nodelibs-fs@0.1.2",
      "ieee754": "npm:ieee754@1.1.8",
      "isarray": "npm:isarray@1.0.0",
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:echarts@3.5.2": {
      "process": "github:jspm/nodelibs-process@0.1.2",
      "zrender": "npm:zrender@3.4.2"
    },
    "npm:inherits@2.0.1": {
      "util": "github:jspm/nodelibs-util@0.1.0"
    },
    "npm:process@0.11.9": {
      "assert": "github:jspm/nodelibs-assert@0.1.0",
      "fs": "github:jspm/nodelibs-fs@0.1.2",
      "vm": "github:jspm/nodelibs-vm@0.1.0"
    },
    "npm:util@0.10.3": {
      "inherits": "npm:inherits@2.0.1",
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:vm-browserify@0.0.4": {
      "indexof": "npm:indexof@0.0.1"
    },
    "npm:zrender@3.4.2": {
      "process": "github:jspm/nodelibs-process@0.1.2"
    }
  }
});
