#!/bin/sh

prefix=""

tar -zxvf ../$prefix/$prefix-1.0-SNAPSHOT-bin.tar.gz

cd ../$prefix/lib

pid=`ps x | grep '$prefix-1.0-SNAPSHOT.jar' | grep -v grep | awk '{print $1}'`


echo "pid:"$pid

 [ -n "$pid" ] &&  kill -9 $pid

new_pid=`ps x | grep $prefix-1.0-SNAPSHOT.jar | grep -v grep | awk '{print $1}'`

echo "新的进程服务pid:"$new_pid

nohup java -jar $prefix-1.0-SNAPSHOT/lib/$prefix-1.0-SNAPSHOT.jar > /dev/null   &
