#!/bin/bash

mkdir -p ../../projects/

cd ../../projects/

projectid=$1

##配置文件生成

cd ../soafw/soafw-config-maven-plugin

#echo `mvn soafw-config:config -Dtemplate=acc.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid/$projectid-dao/src/main/resource/META-INF/config/local`

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

mvn soafw-config:config -Dtemplate=pom.xml -DartifactId=$projectid -DdestDir=../../projects/$projectid




