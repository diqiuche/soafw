#!/bin/bash

mkdir -p ../../projects/

cd ../../projects/

projectid=$1
mvn archetype:generate -DgroupId=com.kjt.service.$projectid -DartifactId=$projectid
cd $projectid
rm -rf src
echo parent build success
#mac
sed -i "" "s/<packaging>jar<\/packaging>/<packaging>pom<\/packaging>/g" pom.xml 
#linux
#sed -i  "s/<packaging>jar<\/packaging>/<packaging>pom<\/packaging>/g" pom.xml 

projectcommon=$projectid-common
mvn archetype:generate -DgroupId=com.kjt.service.$projectid -DartifactId=$projectcommon
echo $projectcommon build success

projectdao=$projectid-dao
mvn archetype:generate -DgroupId=com.kjt.service.$projectid -DartifactId=$projectdao
echo $projectdao build success

projectdomain=$projectid-domain
mvn archetype:generate -DgroupId=com.kjt.service.$projectid -DartifactId=$projectdomain
echo $projectdomain build success

projectrpc=$projectid-rpc
mvn archetype:generate -DgroupId=com.kjt.service.$projectid -DartifactId=$projectrpc
echo $projectrpc build success

projectjob=$projectid-job
mvn archetype:generate -DgroupId=com.kjt.service.$projectid -DartifactId=$projectjob
echo $projectjob build success

projectservice=$projectid-service
mvn archetype:generate -DgroupId=com.kjt.service.$projectid -DartifactId=$projectservice
echo $projectservice build success

projectservice=$projectid-service-impl
mvn archetype:generate -DgroupId=com.kjt.service.$projectid -DartifactId=$projectservice
echo $projectservice build success

projectwebid=$projectid-web
mvn archetype:generate -DgroupId=com.kjt.service.$projectid -DartifactId=$projectwebid -DarchetypeArtifactId=maven-archetype-webapp
echo $projectwebid build success


