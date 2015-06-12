#!/sh/bash

prefix="$1"

app_home_dir="$(dirname $(pwd))"

jetty_pid=`/usr/sbin/lsof -n -P -t -i :$2`
[ -n "$jetty_pid" ] && kill -9 $jetty_pid

nohup java -Ddubbo.shutdown.hook=true -Dapp.home.dir=$app_home_dir -jar ../../jetty-runner.jar --port $2 --log /data1/logs/service/$prefix/requests.log-yyyy_mm_dd ../$prefix-web/target/$prefix-web.war > /dev/null  &