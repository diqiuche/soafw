#!/bin/bash

if [ -z $1 ];then
	echo "请重新输入项目名称"
  	exit
fi

mkdir -p ../../projects/

cd ../../projects/

projectid=$1

app_home_dir="$(pwd)"

if [ ! -d "$app_home_dir/$1" ]; then
	mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectid 
	cd $projectid
	rm -rf src
	echo parent build success
	#mac
	sed -i "" "s/<packaging>jar<\/packaging>/<packaging>pom<\/packaging>/g" pom.xml 
	#linux
	#sed -i  "s/<packaging>jar<\/packaging>/<packaging>pom<\/packaging>/g" pom.xml 
fi

cd $projectid

suffix=""

if [ -n "$2" ]; then
	suffix="-"$2
fi

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

projectwebid=$projectid-web
mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectwebid -DarchetypeArtifactId=maven-archetype-webapp 
echo $projectwebid build success

##配置文件生成

cd ../soafw/soafw-config-maven-plugin

##config
mvn -B soafw-config:config -DartifactId=$projectid -DdestDir=../../projects -Dmodel=AllIn -DgenModule=web -DmoduleSuffix=$2 -X

cd ../../soafw/tools/

sh ./sed-pom.sh $1