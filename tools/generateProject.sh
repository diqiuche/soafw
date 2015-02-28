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

##配置文件生成

cd ../soafw/soafw-config-maven-plugin

mvn soafw-config:config -Dtemplate=acc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-db.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/main/resource/META-INF/config/spring
mvn soafw-config:config -Dtemplate=spring-cache.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/main/resource/META-INF/config/spring

mvn soafw-config:config -Dtemplate=rpc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-rpc/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=mq.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-rpc/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-rpc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-rpc/src/main/resource/META-INF/config/spring

mvn soafw-config:config -Dtemplate=service.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-service.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl/src/main/resource/META-INF/config/spring

mvn soafw-config:config -Dtemplate=web.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/resource/META-INF/config/spring
mvn soafw-config:config -Dtemplate=application-config.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=mvc-config.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/webapp/WEB-INF
mvn soafw-config:config -Dtemplate=pom.xml.web -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web

mvn soafw-config:config -Dtemplate=pom.xml.base.tpl -DartifactId=$projectid -DdestDir=../../projects/$projectid



