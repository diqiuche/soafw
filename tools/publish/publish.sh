#!/bin/bash

######## 参数1表示项目名称

if [ "$1" != "tsl" ]&&[ "$1" != "order" ]&&[ "$1" != "merchant" ]&&[ "$1" != "code" ]&&[ "$1" != "user" ]&&[ "$1" != "purchs" ]&&[ "$1" != "oapi" ];then

  echo "不是有效的项目名称tsl|order|merchant|code|user|purchs|oapi请重新输入"

  exit

fi

shell_gen_base="/root/shell_bash"

source_base="/root/code/projects/workspace"

app_source_path="$source_base/$1"

release_base="/root/apps"

app_release_path="$release_base/$1"

#seq_no="1"
#next_seq_no="2"
date_time=`date +%Y-%m-%d`

#######log_path######
log_path="/data1/logs/service/$1"

global_config_path="/config"

####################

##########app service name #######

app_name="root"

################################

###########初始化版本序号####
org_file=$1"_""seq.txt"

date_file=$1"_""date_version.txt"

if [ ! -f "$date_file" ]; then

   touch $date_file
   echo $date_time  > $date_file

 fi

if [ ! -f "$org_file" ]; then

   touch $org_file
   echo 1 > $org_file

 fi

while read old_date
  do
  echo "发布前的最后日期:"$old_date

  if  [ "$date_time" != "$old_date"  ];then

         echo 1 > $org_file

         echo $date_time  > $date_file
   fi
 done < $date_file


while read seq_num
do
 echo "LINE:"$seq_num
seq_no=$seq_num
done < $1_seq.txt


echo "当前版本号:""$date_time""_"$seq_no

current_version="$date_time""_"$seq_no

next_seq_no=$[ $seq_no + 1 ]

echo "下一个序列号next_seq_no:"$next_seq_no

sed -i 's'/$seq_no/$next_seq_no/'g' $1_seq.txt

global_pom_path="$app_release_path"/"$date_time""_"$seq_no

web_path="$app_release_path"/"$date_time""_"$seq_no/"$1-web"

job_path="$app_release_path"/"$date_time""_"$seq_no/

job_path_syc="$app_release_path"/"$date_time""_"$seq_no/"$1-job-trade-sync"

service_impl_path="$app_release_path"/"$date_time""_"$seq_no/"$1-service-impl"

web_target_path="$app_release_path"/"$date_time""_"$seq_no/"$1-web"/"target"

current_path="$app_release_path"/"$date_time""_""$seq_no"/"bin"

web_src_path="$app_release_path"/"$date_time""_"$seq_no/"$1-web"/"src"

############增加脚本版本文件###########
shell_bash_org_file="$shell_gen_base/$1_seq.txt"
shell_bash_last_file="$shell_gen_base/$1_last_version.txt"
shell_bash_backup_file="$shell_gen_base/$1_last_version_bak.txt"

webPort_xml_file="$app_source_path/$1-web/pom.xml"

webPOM_xml_file="$app_source_path/pom.xml"
###################################

###################项目级别的config############

pro_config_path="$app_release_path"/"config"

###############################################

############加入软连接########

echo "web_path:"$web_path
echo "job_path:"$job_path
echo "service_impl_path:"$service_impl_path

######################生成可执行脚本##########
sed '1,$s/$prefix/'"$1"'/g'  startService_org.sh > startService.sh


echo "输入的第二个参数:"$2

if [  -n "$2" ];then

echo "输入的端口号:"$2

sed -e '1,$s/$2/'"$2"'/g' -e '1,$s/$prefix/'"$1"'/g'  startWeb_org.sh > startWeb.sh

else

str=`sed -n '/<jetty.port>/p' $webPort_xml_file`

#echo "我是user-----str="$str

delblankStr=$(echo $str)

port=${delblankStr:12:4}

echo "未输入端口号默认port:"$port

sed -e  '1,$s/$2/'"$port"'/g' -e '1,$s/$prefix/'"$1"'/g'  startWeb_org.sh > startWeb.sh

fi

sh switch.sh   $1 $current_version

###########################动态生成脚本
declare -a array

i=0

   pro_str="$1"

   pro_len=${#pro_str}

   pro_len=$[ 11 + $pro_len ]

while read line

do
   str=$(echo $line)

  if [[ ${str:1:$pro_len} =  "module>"$1"-job" ]]; then

     echo "读取pom文件:str="$str
    #echo $str
    str2=$(echo ${str#*>})
    # echo "str2:"$str2
    str3=$(echo ${str2%<*})
    #echo "str3:"$str3
    array[$i]=$str3
    i=$[$i + 1]
  fi

done < $webPOM_xml_file


for var in ${array[@]};do

echo "测试数据##########################$var:"  $var

 param1="$1"

 strlen=${#param1}

 start_shell=${var:$strlen:200}

 echo "start_shell:"$start_shell

 start_shell="start"$start_shell

 echo "start_shell=========="$start_shell
 rm -rf "$start_shell".sh

sed -e '1,$s/$2/'"$1"'/g' -e '1,$s/$prefix/'"$var"'/g' startJob_org.sh > "$start_shell".sh

chmod 755 "$start_shell".sh

###########创建路径

if [ ! -d "$job_path$var" ]; then

   mkdir -p  "$job_path$var"
fi

###############打包job############

done

for var in ${array[@]};do

 cd $app_source_path/$var

 mvn -U clean assembly:assembly  ### >  /dev/null

done

################## if apps dir not exists then create #######
if [ ! -d "$release_base" ]; then
   mkdir -p "$release_base"
fi

if [ ! -d "$global_pom_path" ]; then
   mkdir -p "$global_pom_path"
fi

if [ ! -d "$web_path" ]; then
   mkdir -p "$web_path"
fi

if [ ! -d "$service_impl_path" ]; then
   mkdir -p "$service_impl_path"
fi

if [ ! -d "$current_path" ]; then
   mkdir -p "$current_path"
fi


if [ ! -d "$log_path" ]; then
   mkdir -p "$log_path"
fi

if [ ! -d "$global_config_path" ]; then
   mkdir -p "$global_config_path"
fi

if [ ! -d "$web_target_path" ]; then
   mkdir -p "$web_target_path"
fi

if [ ! -d "$web_src_path" ]; then
   mkdir -p "$web_src_path"
fi

if [ ! -d "$pro_config_path" ]; then
   mkdir -p "$pro_config_path"
fi

###########打包 service ##############


cd $app_source_path/$1-service-impl

 mvn -U clean assembly:assembly

########新增配置文件config#########
cp -Rpf $app_source_path/config  $pro_config_path


###################################

cd /root/apps/$1/

rm -rf current

cp -Rpf $app_source_path/pom.xml  $global_pom_path

for var in ${array[@]};do

  param1="$1"

 strlen=${#param1}

 start_shell=${var:$strlen:200}

 start_shell="start"$start_shell

cp -Rpf $app_source_path/$var/target/*.tar.gz  $job_path$var

cp -Rpf $shell_gen_base/$start_shell.sh  $current_path

done

cp -Rpf $app_source_path/$1-service-impl/target/*.tar.gz  $service_impl_path

cp -Rpf $shell_gen_base/startService.sh  $service_impl_path

cp -Rpf $app_source_path/$1-web/pom.xml  $web_path

cp -Rpf $app_source_path/$1-web/target/*.war $app_source_path/$1-web/target/$1-web $web_target_path

cp -Rpf $app_source_path/$1-web/src/* $app_source_path/$1-web/target/$1-web $web_src_path

cp -Rpf $shell_gen_base/current  $app_release_path

cp -Rpf $shell_gen_base/startWeb.sh  $current_path

cp -Rpf $shell_gen_base/startService.sh  $current_path

echo "publish project execute over!!!"

echo "当前版本号请记录:""$date_time""_"$seq_no