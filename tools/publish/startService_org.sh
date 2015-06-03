#!/sh/bash

prefix="$1"

app_home_dir="$(dirname $(pwd))"

$prefix_service_pid=`ps x | grep $prefix-service-impl-1.0-SNAPSHOT.jar | grep -v grep | awk '{print $1}'`


echo "prefix_service_pid:"$$prefix_service_pid

 [ -n "$"$prefix"_service_pid" ] && kill -9 $$prefix_service_pid

tar -zxvf ../$prefix-service-impl/$prefix-service-impl-1.0-SNAPSHOT-bin.tar.gz

nohup java -Dapp.home.dir=$app_home_dir -jar $prefix-service-impl-1.0-SNAPSHOT/lib/$prefix-service-impl-1.0-SNAPSHOT.jar >/dev/null  &