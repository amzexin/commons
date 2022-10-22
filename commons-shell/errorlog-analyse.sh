#!/bin/bash
if [ $# == 0 ]; then
    echo "==================================脚本介绍=================================="
    echo "脚本逻辑: "
    echo "第一步: 进入日志压缩文件所在目录"
    echo "第二步: 解压日志文件(从开始日期到6天后的所有压缩文件)"
    echo "第三步: 记录error发生的位置, 并统计发生的次数"
    echo "参数说明: "
    echo "-l       即: log directory            日志压缩文件所在目录 (可选: 默认当前文件夹)"
    echo "-s       即: skip unzip               跳过解压直接进行分析, 如果已经被脚本解压过的话"
    echo "-e       即: extract directory        存放解压文件和分析结果的目录 (必须提前创建好)"
    echo "-S       即: start date               指定从哪一天的日志开始进行日志分析 (格式: yyyy-MM-dd和yyyy/MM/dd均可)"
    echo "=========================================================================="
    exit 1
fi

logDir=""
skipUnzip="n"
extDir=""
startDate=""
while getopts "l:se:S:" opt; do
    case $opt in
    l)
        logDir=$OPTARG
        ;;
    s)
        skipUnzip='y'
        ;;
    e)
        extDir=$OPTARG
        ;;
    S)
        startDate=$OPTARG
        ;;
    ?)
        exit 1
        ;;
    esac
done

if [ "${logDir}" != "" ]; then
    if [ ! -d "${logDir}" ]; then
        echo "ERROR: ${logDir}目录不存在"
        exit 1
    fi
    cd ${logDir}
    echo "进入log directory: $(pwd)"
fi

if [ "${extDir}" == "" ]; then
    echo "ERROR: 请通过-e 设置extract directory"
    exit 1
fi

if [ "${startDate}" == "" ]; then
    echo "ERROR: 请通过-S 设置start date"
    exit 1
fi

# 将开始日期转换为YYYY-MM-dd的格式
someday=$(date -d "${startDate}" +%F)
if [ "${someday}" == "" ]; then
    echo "ERROR: 命令[date -d ${startDate} +%F]执行时发生错误"
    exit 1
fi

if [ $skipUnzip != 'y' ]; then
    if [ ! -d "${extDir}" ]; then
        mkdir -p "${extDir}"
        echo "创建extract directory: ${extDir}"
    fi
    for i in {0..6}; do
        # 获取someday $i天之后的一个日期
        file_date=$(date -d "+$i day ${someday}" +%F)

        # 日志压缩文件名称格式: error-{YYYY-MM-dd}_{digit}.log.zip
        file_name_arr=$(ls | grep -e "error-${file_date}_[[:digit:]].log.zip")
        for file_name in ${file_name_arr[*]}; do
            new_file_name=${extDir}/${someday}/${file_name%.zip}
            echo "解压${file_name} ==> ${new_file_name}"
            unzip "$file_name" -d "${extDir}/${someday}"
        done
    done
fi

# printf("%s %s %s",$4,$5,$6); 打印第4、5、6列
# for (i=10;i<=NF;i++)printf("%s ", $i); 从第10列打印到最后一列
# print "" 打印换行
cat ${extDir}/${someday}/error-*.log | grep 'ERROR' | awk '{print $4,$5,$6}' | sort | uniq -c | sort -rn >${extDir}/analyze_simple.txt
cat ${extDir}/${someday}/error-*.log | grep 'ERROR' | awk '{printf("%s %s %s",$4,$5,$6); for (i=10;i<=NF;i++)printf(" %s", $i);print ""}' | sort | uniq -c | sort -rn >${extDir}/analyze.txt

# 为了避免误删不做自动删除
echo "error已分析完毕, 分析结果详见${extDir}/analyze.txt和${extDir}/analyze_simple.txt, 并记得删除解压后的文件"

<<comment
《后续分析可能会用到的命令》
1. 获取错误信息对应的文件
命令: grep -Ri {目录名} -e "{要查询的内容}"
-R 是用于递归子目录，可以查询当前目录下的所有子目录包含特定查找数据的目录
-i 的含义是不区分大小写
例如: grep -Ri 2022-10-13/ -e "o.h.e.jdbc.spi.SqlExceptionHelper" | awk '{print $1,$2}' | sort | uniq -c | sort -k 2,3 > SqlExceptionHelper.txt


comment
