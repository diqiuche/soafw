#!/sh/bash

prefix="$1"

$prefix_service_pid=`ps x | grep $prefix-service-impl-1.0-SNAPSHOT.jar | grep -v grep | awk '{print $1}'`


echo "prefix_service_pid:"$$prefix_service_pid

 [ -n "$prefix_service_pid" ] && kill -9 $$prefix_service_pid

tar -zxvf ../$prefix-service-impl/$prefix-service-impl-1.0-SNAPSHOT-bin.tar.gz
cd ../prefix-service-impl/lib

#$1_service_pid=`ps x | grep $1-service-impl-1.0-SNAPSHOT.jar | grep -v grep | awk '{print $1}'`

#$1-service_pid=`ps x | grep $1-service | grep -v grep  | awk '{print $1}'`

nohup java -jar $prefix-service-impl-1.0-SNAPSHOT/lib/$prefix-service-impl-1.0-SNAPSHOT.jar >/dev/null  &
~
