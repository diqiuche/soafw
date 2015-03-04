#!/bin/bash

mkdir -p ../../projects/

cd ../../projects/

projectid=$1

##配置文件生成

cd ../soafw/soafw-config-maven-plugin/

##project
mvn soafw-config:config -Dtemplate=pom.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid

##common
mvn soafw-config:config -Dtemplate=pom.xml.common -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-common

##config
mvn soafw-config:config -Dtemplate=pom.xml.config -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-config

##cache
mvn soafw-config:config -Dtemplate=pom.xml.cache -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-cache
mvn soafw-config:config -Dtemplate=cache.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-cache/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-cache.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-cache/src/main/resource/META-INF/config/spring

##dao
mvn soafw-config:config -Dtemplate=pom.xml.dao -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao
mvn soafw-config:config -Dtemplate=acc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-db.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/main/resource/META-INF/config/spring


##rpc
mvn soafw-config:config -Dtemplate=pom.xml.rpc -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-rpc
mvn soafw-config:config -Dtemplate=rpc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-rpc/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-rpc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-rpc/src/main/resource/META-INF/config/spring

##mq
mvn soafw-config:config -Dtemplate=pom.xml.mq -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-mq
mvn soafw-config:config -Dtemplate=mq.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-mq/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-mq.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-mq/src/main/resource/META-INF/config/spring

##service
mvn soafw-config:config -Dtemplate=pom.xml.svc -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service

##job
mvn soafw-config:config -Dtemplate=pom.xml.job -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-job
mvn soafw-config:config -Dtemplate=job.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-job/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-job.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-job/src/main/resource/META-INF/config/spring

##service impl
mvn soafw-config:config -Dtemplate=pom.xml.svc -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl
mvn soafw-config:config -Dtemplate=service.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-service.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl/src/main/resource/META-INF/config/spring

##web
mvn soafw-config:config -Dtemplate=pom.xml.web -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web
mvn soafw-config:config -Dtemplate=web.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/resource/META-INF/config/spring
mvn soafw-config:config -Dtemplate=application-config.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=mvc-config.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/webapp/WEB-INF





