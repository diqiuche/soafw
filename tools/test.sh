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
mvn soafw-config:config -Dsufix=md -Dtemplate=README.md.cache -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-cache
mvn soafw-config:config -Dtemplate=cache.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-cache/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dsufix=properties -Dtemplate=cache-mem.properties -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-cache/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dsufix=properties -Dtemplate=cache-redis.properties -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-cache/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-cache.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-cache/src/main/resource/META-INF/config/spring

##dao
mvn soafw-config:config -Dtemplate=pom.xml.dao -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao
mvn soafw-config:config -Dsufix=md -Dtemplate=README.md.dao -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao
mvn soafw-config:config -Dtemplate=acc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dsufix=properties -Dtemplate=database.properties -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-db.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/main/resource/META-INF/config/spring
mvn soafw-config:config -Dtemplate=spring-dao.gen.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/main/resource
mvn soafw-config:config -Dsufix=java -Dtemplate=DaoGen.java -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/test/java/com/kjt/service/tsl/dao


##rpc
mvn soafw-config:config -Dtemplate=pom.xml.rpc -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-rpc
mvn soafw-config:config -Dtemplate=rpc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-rpc/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-rpc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-rpc/src/main/resource/META-INF/config/spring

##mq
mvn soafw-config:config -Dtemplate=pom.xml.mq -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-mq
mvn soafw-config:config -Dtemplate=mq.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-mq/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-mq.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-mq/src/main/resource/META-INF/config/spring

##domain
mvn soafw-config:config -Dtemplate=pom.xml.domain -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-domain

##service
mvn soafw-config:config -Dtemplate=pom.xml.svc -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service

##job
mvn soafw-config:config -Dtemplate=pom.xml.job -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-job
mvn soafw-config:config -Dtemplate=job.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-job/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-job.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-job/src/main/resource/META-INF/config/spring

##web
mvn soafw-config:config -Dtemplate=pom.xml.web -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web
mvn soafw-config:config -Dtemplate=web.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/resource/META-INF/config/spring
mvn soafw-config:config -Dtemplate=spring-web.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-mvc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-web/src/main/webapp/WEB-INF

##service impl
mvn soafw-config:config -Dtemplate=pom.xml.svc.impl -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl
mvn soafw-config:config -Dtemplate=service.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl/src/main/resource/META-INF/config/local
mvn soafw-config:config -Dtemplate=spring-service.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-service-impl/src/main/resource/META-INF/config/spring

