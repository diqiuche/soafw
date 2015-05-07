#!/bin/sh

 #首先需要先输入参数$ 1-工程名(tsl/order/merchant) 2-版本号

#echo -n "请输入你要发布的版本号version,格式例如:2015-03-19_2:"

#首先判断版本号文件是否已存在 如果不存在则创建,存在则备份 并更新当前文件内容

 echo "输入的参数:"$1" "$2

# echo "$1 = tsl"

 org_file=$1"_""seq.txt"

 version_file=$1"_""last_version.txt"

 version_file_old=$1"_""last_version_bak.txt"

 ##########保存版本所属的日期

 current_date=`date +%Y-%m-%d`

 date_file=$1"_""date_version.txt"


 if [ $# != 2 ];then

  echo "参数数量不是2个,清核对 项目名称、版本号"

  exit

fi

#### 校验工程名是否是(tsl/order/merchant)等等 #####

#value=[ "$1" != "tsl" ]

#echo "value:::::"$value

#if [ "$1" != "tsl" ]&&[ "$1" != "order" ]&&[ "$1" != "merchant" ];then

#  echo "不是有效的项目名称tsl/order/merchant请重新输入"

#  exit

#fi


 if [ ! -f "$org_file" ]; then

   touch $org_file
   echo 1 > $org_file

 fi



 if [ ! -f "$version_file" ]; then

   touch $version_file
   echo $2 > $version_file

 fi

 if [ ! -f "$version_file_old" ]; then

   touch $version_file_old
   echo $2 > $version_file_old

 fi

  echo "当前准备同步的版本号:"$2

 while read oldversion
  do
  echo "发布前的最后版本版本号:"$oldversion
  echo $oldversion > $version_file_old

 done < $version_file

 echo $2 > $version_file

 echo "switch backup success#######"

 #修改当前的软连接设置

   rm -rf current

  ln  -s $2/bin   current

# sed -i 's/current_version/$version/g' current.sh
# rm -f current.sh
# echo "#!/bin/sh" >> current.sh
# echo "cd aaa/$version/bin" >>current.sh


##########更新当前软连接为当前版本