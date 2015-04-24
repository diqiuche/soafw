#!/bin/sh

prefix="$1"

tar -zxvf ../$prefix-job-trade-sync/$prefix-job-trade-sync-1.0-SNAPSHOT-bin.tar.gz

cd ../$prefix-job-trade-sync/lib

prefix_job_trade_sync_pid=`ps x | grep $prefix-job-trade-sync-1.0-SNAPSHOT.jar | grep -v grep | awk '{print $1}'`

#$1_job_trade_sync_pid=`ps x | grep $1-job-trade-sync | grep -v grep  | awk '{print $1}'`

echo "$prefix_job_trade_sync_pid:"$prefix_job_trade_sync_pid

 [ -n "prefix_job_trade_sync_pid" ] &&  kill -9 $"$prefix_job_trade_sync_pid"

echo "success"

nohup java -jar $prefix-job-trade-sync-1.0-SNAPSHOT/lib/$prefix-job-trade-sync-1.0-SNAPSHOT.jar > /dev/null   &
