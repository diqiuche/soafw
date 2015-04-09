#!/bin/bash

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

projectcommon=$projectid-common
mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectcommon 
echo $projectcommon build success

projectconfig=$projectid-config
mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectconfig 
echo $projectconfig build success

projectcache=$projectid-cache
mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectcache 
echo $projectcache build success

projectdao=$projectid-dao
mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectdao 
echo $projectdao build success

projectrpc=$projectid-rpc
mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectrpc 
echo $projectrpc build success

projectmq=$projectid-mq
mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectmq 
echo $projectmq build success

projectdomain=$projectid-domain
mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectdomain 
echo $projectdomain build success

projectservice=$projectid-service
mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectservice 
echo $projectservice build success

projectjob=$projectid-job
if [ -n "$2" ]; then
	projectjob=$projectjob-$2
fi

mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectjob 
echo $projectjob build success

projectservice=$projectid-service-impl
mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectservice 
echo $projectservice build success

projectwebid=$projectid-web
mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectwebid -DarchetypeArtifactId=maven-archetype-webapp 
echo $projectwebid build success

##配置文件生成

cd ../soafw/soafw-config-maven-plugin

##config
mvn -B soafw-config:config -DartifactId=$projectid -DdestDir=../../projects -Dmodel=AllIn -DmoduleSuffix=$2 -X
