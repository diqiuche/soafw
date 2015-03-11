#!/bin/bash

mkdir -p ../../projects/

cd ../../projects/

projectid=$1
mvn archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectid 
cd $projectid
rm -rf src
echo parent build success
#mac
sed -i "" "s/<packaging>jar<\/packaging>/<packaging>pom<\/packaging>/g" pom.xml 
#linux
#sed -i  "s/<packaging>jar<\/packaging>/<packaging>pom<\/packaging>/g" pom.xml 

projectcommon=$projectid-common
mvn archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectcommon 
echo $projectcommon build success

projectconfig=$projectid-config
mvn archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectconfig 
echo $projectconfig build success

projectcache=$projectid-cache
mvn archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectcache 
echo $projectcache build success

projectdao=$projectid-dao
mvn archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectdao 
echo $projectdao build success

projectrpc=$projectid-rpc
mvn archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectrpc 
echo $projectrpc build success

projectmq=$projectid-mq
mvn archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectmq 
echo $projectmq build success

projectdomain=$projectid-domain
mvn archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectdomain 
echo $projectdomain build success

projectservice=$projectid-service
mvn archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectservice 
echo $projectservice build success

projectjob=$projectid-job
mvn archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectjob 
echo $projectjob build success

projectservice=$projectid-service-impl
mvn archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectservice 
echo $projectservice build success

projectwebid=$projectid-web
mvn archetype:generate -DarchetypeCatalog=locale -DgroupId=com.kjt.service.$projectid -DartifactId=$projectwebid -DarchetypeArtifactId=maven-archetype-webapp 
echo $projectwebid build success

##配置文件生成

cd ../soafw/soafw-config-maven-plugin

##project
mvn soafw-config:config -Dtemplate=pom.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid

##common
mvn soafw-config:config -Dtemplate=pom.xml.common -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-common

##config
mvn soafw-config:config -Dtemplate=pom.xml.config -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-config
mvn soafw-config:config -Dsufix=md -Dtemplate=README.md.config -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-config

##cache
mvn soafw-config:config -Dtemplate=pom.xml.cache -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-cache
mvn soafw-config:config -Dsufix=md -Dtemplate=README.md.cache -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-cache
mvn soafw-config:config -Dtemplate=cache.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-cache/src/main/resources/META-INF/config/local
mvn soafw-config:config -Dsufix=properties -Dtemplate=cache-mem.properties -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-cache/src/main/resources/META-INF/config/local
mvn soafw-config:config -Dsufix=properties -Dtemplate=cache-redis.properties -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-cache/src/main/resources/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-cache.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-cache/src/main/resources/META-INF/config/spring

##dao
mvn soafw-config:config -Dtemplate=pom.xml.dao -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao
mvn soafw-config:config -Dsufix=md -Dtemplate=README.md.dao -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao
mvn soafw-config:config -Dtemplate=acc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/main/resources/META-INF/config/local
mvn soafw-config:config -Dsufix=properties -Dtemplate=database.properties -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/main/resources/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-db.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/main/resources/META-INF/config/spring
mvn soafw-config:config -Dsufix=java -Dtemplate=DaoGen.java -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/test/java/com/kjt/service/$projectid/dao


##rpc
mvn soafw-config:config -Dtemplate=pom.xml.rpc -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-rpc
mvn soafw-config:config -Dtemplate=rpc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-rpc/src/main/resources/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-rpc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-rpc/src/main/resources/META-INF/config/spring

##mq
mvn soafw-config:config -Dtemplate=pom.xml.mq -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-mq
mvn soafw-config:config -Dtemplate=mq.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-mq/src/main/resources/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-mq.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-mq/src/main/resources/META-INF/config/spring

##domain
mvn soafw-config:config -Dtemplate=pom.xml.domain -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-domain

##service
mvn soafw-config:config -Dtemplate=pom.xml.svc -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service

##job
mvn soafw-config:config -Dtemplate=pom.xml.job -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-job
mvn soafw-config:config -Dsufix=md -Dtemplate=README.md.job -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-job
mvn soafw-config:config -Dtemplate=job.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-job/src/main/resources/META-INF/config/local
mvn soafw-config:config -Dsufix=properties -Dtemplate=dubbo.properties -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-job/src/main/resources/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-job.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-job/src/main/resources/META-INF/config/spring
mvn soafw-config:config -Dsufix=java -Dtemplate=Startup.java.job -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-job/src/main/java/com/kjt/service/$projectid/job
mvn soafw-config:config -Dtemplate=dist.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-job/src/main/assemble

##web
mvn soafw-config:config -Dtemplate=pom.xml.web -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web
mvn soafw-config:config -Dtemplate=web.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/webapp/WEB-INF
mvn soafw-config:config -Dtemplate=logback.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/webapp/WEB-INF
mvn soafw-config:config -Dtemplate=webapp.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/resources/META-INF/config/local
mvn soafw-config:config -Dsufix=properties -Dtemplate=dubbo.properties -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/resources/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-dubbo.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/resources/META-INF/config/spring
mvn soafw-config:config -Dtemplate=spring-mvc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/resources/META-INF/config/spring

##service impl
mvn soafw-config:config -Dtemplate=pom.xml.svc.impl -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl
mvn soafw-config:config -Dsufix=md -Dtemplate=README.md.service -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl
mvn soafw-config:config -Dtemplate=service.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl/src/main/resources/META-INF/config/local
mvn soafw-config:config -Dsufix=properties -Dtemplate=dubbo.properties -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl/src/main/resources/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-service.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl/src/main/resources/META-INF/config/spring
mvn soafw-config:config -Dsufix=java -Dtemplate=Startup.java.service -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl/src/main/java/com/kjt/service/$projectid
mvn soafw-config:config -Dtemplate=dist.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl/src/main/assemble
