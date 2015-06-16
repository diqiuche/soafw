#!/bin/bash

if [ -z $1 ];then
	echo "请重新输入项目名称"
  	exit
fi

mkdir -p ../../projects/

cd ../../projects/

projectid=$1
mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectid 
cd $projectid
rm -rf src
echo parent build success
#mac
sed -i "" "s/<packaging>jar<\/packaging>/<packaging>pom<\/packaging>/g" pom.xml 
#linux
#sed -i  "s/<packaging>jar<\/packaging>/<packaging>pom<\/packaging>/g" pom.xml 

if [ ! -d "$app_home_dir/$1/$1-common" ]; then
	projectcommon=$projectid-common
	mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectcommon 
	echo $projectcommon build success
fi

if [ ! -d "$app_home_dir/$1/$1-config" ]; then
	projectconfig=$projectid-config
	mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectconfig 
	echo $projectconfig build success
fi

if [ ! -d "$app_home_dir/$1/$1-cache" ]; then
	projectcache=$projectid-cache
	mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectcache 
	echo $projectcache build success
fi

if [ ! -d "$app_home_dir/$1/$1-dao" ]; then
	projectdao=$projectid-dao
	mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectdao 
	echo $projectdao build success
fi

if [ ! -d "$app_home_dir/$1/$1-rpc" ]; then
	projectrpc=$projectid-rpc
	mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectrpc 
	echo $projectrpc build success
fi

if [ ! -d "$app_home_dir/$1/$1-mq" ]; then
	projectmq=$projectid-mq
	mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectmq 
	echo $projectmq build success
fi

if [ ! -d "$app_home_dir/$1/$1-domain" ]; then
	projectdomain=$projectid-domain
	mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectdomain 
	echo $projectdomain build success
fi

if [ ! -d "$app_home_dir/$1/$1-service" ]; then
	projectservice=$projectid-service
	mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectservice 
	echo $projectservice build success
fi

projectservice=$projectid-service-impl
mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectservice 
echo $projectservice build success

##配置文件生成

cd ../soafw/soafw-config-maven-plugin

##config
mvn -B soafw-config:config -DartifactId=$projectid -DdestDir=../../projects -Dmodel=AllIn -DgenModule=service -DmoduleSuffix=$2 -X

cd ../../soafw/tools/

sh ./sed-pom.sh $1