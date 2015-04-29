#!/sh/bash

# 参数的说明  1-项目名(tsl) 2-版本号(2015-03-19_1) 3-需要同步的远程IP地址(192.168.1.111)

if [ -z $4 ]; then
     user="app"
else
    user=$4
fi

echo "$user"

user_home="/home/$user"

#apps_local_path="/root/apps"
apps_path="$user_home/apps/$1/$2"

#######log_path######
log_path="/data1/logs/service/$1"
#####################

########global_config#####

global_config_path="/config"

####################

###########pro_config_path####

pro_config_path="$user_home"/"apps"/"$1"/"config"
#############################


################
soft_link_path="/root/apps/$1/current"


if [ $# > 5 ];then

  echo "参数数量不是4个,清核对 项目名称、版本号、远程IP地址"

  #exit

fi

#### 校验工程名是否是(tsl/order/merchant)等等 #####


if [ "$1" != "tsl" ]&&[ "$1" != "order" ]&&[ "$1" != "merchant" ]&&[ "$1" != "code" ]&&[ "$1" != "user" ]&&[ "$1" != "purchs" ];then

  echo "不是有效的项目名称tsl/order/merchant/user请重新输入"

  exit

fi



ssh $user@$3  "test -d $apps_path||mkdir -p $apps_path"

ssh $user@$3  "test -d $log_path||mkdir -p $log_path"

ssh $user@$3  "test -d $global_config_path||mkdir -p $global_config_path"

ssh $user@$3  "test -d $pro_config_path||mkdir -p $pro_config_path"

#ssh $user@$3  "test ! -d $apps_path||rm -rf $soft_link_path "

ssh $user@$3  "test ! -d $apps_path||(cd $user_home/apps/$1;rm -rf current)"

rsync -aou -vzrtopg --delete --progress /root/apps/$1/$2/*  $user@"$3":/$user_home/apps/$1/$2/

rsync -aou -vzrtopg --delete --progress /root/apps/$1/current  $user@"$3":/$user_home/apps/$1/

rsync -aou -vzrtopg --delete --progress "$global_config_path"/*  root@"$3":$global_config_path

#rsync -aou -vzrtopg --delete --progress  --exclude "*.log" "$log_path"/*  app@"$3":$log_path

rsync -aou -vzrtopg --delete --progress "$pro_config_path"/*  $user@"$3":$pro_config_path




