#!/bin/bash

mkdir -p ../../projects/

cd ../../projects/

projectid=$1 

cd $projectid

suffix=""

if [ -n "$2" ]; then
	suffix="-"$2
fi

projectjob=$projectid-job$suffix
mvn -B archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectjob 
echo $projectjob build success

##配置文件生成

cd ../soafw/soafw-config-maven-plugin

##config
mvn -B soafw-config:config -DartifactId=$projectid -DdestDir=../../projects -Dmodel=AllIn -DgenModule=job -DmoduleSuffix=$2 -X
