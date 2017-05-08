#!/bin/bash
param_num=$#

#参数检测函数
function param_check () {
	 if [ $param_num == 0 ] ; then
		 echo >&2 "you must input param,the first is module name";exit 1;
	 fi
}

#执行参数检测
param_check

#软件包检测
command -v git >/dev/null 2>&1 || { echo >&2 "git is required,but it's not installed,Aborting"; exit 1;}
command -v mvn >/dev/null 2>&1 || { echo >&2 "mvn is required,but it's not installed,Aborting"; exit 1;}
command -v gradle >/dev/null 2>&1 || { echo >&2 "gradle is required,but it's not installed,Aborting"; exit 1;}
command -v npm >/dev/null 2>&1 || { echo >&2 "npm is required,but it's not installed,Aborting"; exit 1;}
command -v jspm >/dev/null 2>&1 || { echo >&2 "jspm is required,but it's not installed,Aborting"; exit 1;}

module_name=$1

if "$module_name" in "config"  "download" "link"  "resolve"  "data"  ; then
	echo '准备启动$(module_name)'
else	
	echo >&2 ' param error,the param is not a module name'; exit 1;
fi

cd task-processor || { echo >&2 "not found task-processor module";exit 1;}

gradle publishToMavenLocal

cd ../taskRpc || { echo >&2 "not found taskRpc module";exit 1;}
mvn clean install

cd ../$module_name || { echo >&2 "not found  module";exit 1;}
mvn clean install

cd target || { echo >&2 "build module fail,check the code";exit 1;}

java -jar "$module_name"-0.0.1-SNAPSHOT.jar 0 9001 192.168.1.3

