#!/bin/bash
# 进入arg3目录, 将arg1日期之后一星期的日志, 解压到arg3/arg2目录下

paramCount=$#
if [ ${paramCount} == 0 ]; then
    echo "请输入参数!!! arg1: 开始日期; arg1: 临时文件夹名称; arg3: 压缩文件所在目录(若为当前目录可不填)"
    exit 1
elif [ ${paramCount} == 1 ]; then
    echo "请输入参数!!! arg2: 临时文件夹名称; arg3: 压缩文件所在目录(若为当前目录可不填)"
    exit 1
elif [ ${paramCount} == 3 ]; then
    cd $3
    echo "已进入$(pwd)"
fi

# 将$1转换为YYYY-MM-dd的格式
someday=$(date -d "$1" +%F)
echo "开始日期为$someday"

for i in {0..6}; do
    # 获取someday $i天之后的一个日期
    file_date=$(date -d "+$i day $someday" +%F)

    # 日志压缩文件名称格式: error-{YYYY-MM-dd}_{digit}.log.zip
    file_name_arr=$(ls | grep -e "error-${file_date}_[[:digit:]].log.zip")
    for file_name in ${file_name_arr[*]}; do
        new_file_name=$2/${file_name%.zip}
        echo "==>开始解压$file_name"
        unzip "$file_name" -d "$2"
        echo "解压后的文件名是$new_file_name"
    done
done

cat error-*.log | grep 'ERROR' | awk '{printf("%s %s %s",$4,$5,$6); for (i=10;i<=NF;i++)printf("%s ", $i);print ""}'  | sort | uniq -c | sort -rn > analyze.txt

