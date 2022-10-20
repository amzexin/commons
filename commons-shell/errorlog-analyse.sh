#!/bin/bash
# 进入arg4目录, 将arg1日期7天后的日志解压到arg3/arg2目录下, 并统计error发生的位置和次数
arg1="arg1: 开始日期"
arg2="arg2: 临时文件夹名称"
arg3="arg3: 是否需要解压(y/n)"
arg4="arg4: 压缩文件所在目录(若为当前目录可不填)"

paramCount=$#
if [ ${paramCount} == 0 ]; then
    echo "请输入参数!!! ${arg1}, ${arg2}, ${arg3}, ${arg4}"
    exit 1
elif [ ${paramCount} == 1 ]; then
    echo "请输入参数!!! ${arg2}, ${arg3}, ${arg4}"
    exit 1
elif [ ${paramCount} == 2 ]; then
    echo "请输入参数!!! ${arg3}, ${arg4}"
    exit 1
elif [ ${paramCount} == 4 ]; then
    cd $4
    echo "已进入$(pwd)"
fi

# 将$1转换为YYYY-MM-dd的格式
someday=$(date -d "$1" +%F)
echo "开始日期为$someday"

if [ $3 == 'y' ]; then
    for i in {0..6}; do
        # 获取someday $i天之后的一个日期
        file_date=$(date -d "+$i day $someday" +%F)

        # 日志压缩文件名称格式: error-{YYYY-MM-dd}_{digit}.log.zip
        file_name_arr=$(ls | grep -e "error-${file_date}_[[:digit:]].log.zip")
        for file_name in ${file_name_arr[*]}; do
            new_file_name=$2/$someday/${file_name%.zip}
            echo "解压${file_name} ==> ${new_file_name}"
            unzip "$file_name" -d "$2/$someday"
        done
    done
fi

# printf("%s %s %s",$4,$5,$6); 打印第4、5、6列
# for (i=10;i<=NF;i++)printf("%s ", $i); 从第10列打印到最后一列
# print "" 打印换行
cat $2/$someday/error-*.log | grep 'ERROR' | awk '{print $4,$5,$6}' | sort | uniq -c | sort -rn >$2/analyze_simple.txt
cat $2/$someday/error-*.log | grep 'ERROR' | awk '{printf("%s %s %s",$4,$5,$6); for (i=10;i<=NF;i++)printf(" %s", $i);print ""}' | sort | uniq -c | sort -rn >$2/analyze.txt

# 为了避免误删不做自动删除
echo "error已分析完毕, 分析结果详见$2/analyze.txt和$2/analyze_simple.txt, 并记得删除解压后的文件"
