#!/bin/sh

prefix="$1"

tar -zxvf ../$prefix-job/$prefix-job-1.0-SNAPSHOT-bin.tar.gz
cd ../$prefix-job/lib


$prefix_job_pid=`ps x | grep $prefix-job-1.0-SNAPSHOT.jar | grep -v grep | awk '{print $1}'`

#$1_job_pid=`ps x | grep $1-job | grep -v grep  | awk '{print $1}'`

echo "$prefix_job_pid:"$$prefix_job_pid

[ -n "$prefix_job_pid" ] && kill -9 $$prefix_job_pid

echo "success"

nohup java -jar $prefix-job-1.0-SNAPSHOT/lib/$prefix-job-1.0-SNAPSHOT.jar &
