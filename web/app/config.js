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
    "angular-material": "github:angular/bower-material@1.1.4",
    "angular-messages": "github:angular/bower-angular-messages@1.6.4",
    "angular-ui-router": "npm:angular-ui-router@0.4.2",
    "css": "github:systemjs/plugin-css@0.1.33",
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
    "github:angular/bower-material@1.1.4": {
      "angular": "github:angular/bower-angular@1.6.4",
      "angular-animate": "github:angular/bower-angular-animate@1.6.4",
      "angular-aria": "github:angular/bower-angular-aria@1.6.4",
      "css": "github:systemjs/plugin-css@0.1.33"
    },
    "github:jspm/nodelibs-assert@0.1.0": {
      "assert": "npm:assert@1.4.1"
    },
    "github:jspm/nodelibs-buffer@0.1.1": {
      "buffer": "npm:buffer@5.0.6"
    },
    "github:jspm/nodelibs-process@0.1.2": {
      "process": "npm:process@0.11.10"
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
      "buffer": "github:jspm/nodelibs-buffer@0.1.1",
      "process": "github:jspm/nodelibs-process@0.1.2",
      "util": "npm:util@0.10.3"
    },
    "npm:buffer@5.0.6": {
      "base64-js": "npm:base64-js@1.2.0",
      "ieee754": "npm:ieee754@1.1.8"
    },
    "npm:inherits@2.0.1": {
      "util": "github:jspm/nodelibs-util@0.1.0"
    },
    "npm:process@0.11.10": {
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
    }
  }
});
