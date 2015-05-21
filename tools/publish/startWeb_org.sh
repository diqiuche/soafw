#!/sh/bash

prefix="$1"


jetty_pid=`/usr/sbin/lsof -n -P -t -i :$2`
[ -n "$jetty_pid" ] && kill -9 $jetty_pid

#cd /home/app/apps/$1/$2/$1-web

nohup java -jar ../../jetty-runner.jar --port $2 --log /data1/logs/service/$prefix/requests.log-yyyy_mm_dd ../$prefix-web/target/$prefix-web.war > /dev/null  &