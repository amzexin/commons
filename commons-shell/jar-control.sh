jar_name="xxx.jar"
deploy_path="/root/runspace/xxx"
filePath="${deploy_path}/${jar_name}"
# jar包在本地的位置
localFilePath="/xxx/xxx.jar"

StopTest() {
    echo 'Stopping test'
    reTryCount=3
    for ((i = 0; i < reTryCount; ++i)); do
        sleep 1
        startResult=$(curl http://localhost:8888/graceful-shutdown-test/stop -s) # -s: 表示不显示统计信息
        if [ ${startResult} ]; then
            echo 'Stop Success!'
            break
        elif ((i == reTryCount - 1)); then
            echo "Stop Failed!" # 不换行的打印"."
            exit 1
        else
            echo -n "." # 不换行的打印"."
        fi
    done
}

StartTest() {
    echo 'Starting test'
    reTryCount=3
    for ((i = 0; i < reTryCount; ++i)); do
        sleep 1
        startResult=$(curl http://localhost:8888/graceful-shutdown-test/start?authToken=69f4105e517d4186b652ad122a529e53\&env=dev2 -s) # -s: 表示不显示统计信息
        if [ "${startResult}" ]; then
            echo 'StartTest Success!'
            break
        elif ((i == reTryCount - 1)); then
            echo "StartTest Failed!" # 不换行的打印"."
            exit 1
        else
            echo -n "." # 不换行的打印"."
        fi
    done

}

Stop() {
    pid=$(ps -ef | grep "${jar_name}" | grep -v grep | grep -v kill | awk '{print $2}')
    echo 'Stopping '${pid}
    if [ ${pid} ]; then
        kill ${pid}
        for ((i = 0; i < 10; ++i)); do
            sleep 1
            pid=$(ps -ef | grep "${jar_name}" | grep -v grep | grep -v kill | awk '{print $2}')
            if [ ${pid} ]; then
                echo -n "." # 不换行的打印"."
            else
                echo 'Stop Success!'
                break
            fi
        done
        pid=$(ps -ef | grep "${jar_name}" | grep -v grep | grep -v kill | awk '{print $2}')
        if [ ${pid} ]; then
            echo 'Kill Process!'
            kill -9 ${pid}
        fi
    else
        echo 'App already stop!'
    fi
}

Start() {
    cd ${deploy_path}
    pid=$(ps -ef | grep -n "${jar_name}" | grep -v grep | grep -v kill | awk '{print $2}')
    if [ ${pid} ]; then
        echo 'App already start!'
    else
        echo 'Starting jar'
        nohup java -jar ${filePath} --server.port=8888 --spring.profiles.active=dev2 >/dev/null 2>&1 &
        reTryCount=10
        for ((i = 0; i < reTryCount; ++i)); do
            sleep 1
            startResult=$(curl http://localhost:8888/ping -s) # -s: 表示不显示统计信息
            if [ ${startResult} ]; then
                echo 'Start Success!'
                break
            elif ((i == reTryCount - 1)); then
                echo "Start Failed!" # 不换行的打印"."
                exit 1
            else
                echo -n "." # 不换行的打印"."
            fi
        done
    fi
}

RemoveJar() {
    echo 'RemoveJar '${filePath}
    rm ${filePath}
    echo 'RemoveJar Success!'
}

UploadJar() {
    echo 'Uploading Jar'
    scp ${localFilePath} root@gx_alpha:${filePath}
    echo 'UploadJar Success!'
}

for cmd in "$@"; do
    case ${cmd} in
    "start")
        Start
        ;;
    "start_test")
        StartTest
        ;;
    "stop")
        Stop
        ;;
    "stop_test")
        StopTest
        ;;
    "rm_jar")
        RemoveJar
        ;;
    "up_jar")
        UploadJar
        ;;
    *)
        echo "unknown command"
        exit 1
        ;;
    esac
done

<<comment
前提：配置ssh

连接远程服务器，并执行命令
ssh root@server_ip 'bash -s' < control.sh start start_test

上传jar到远程服务器
./control.sh up_jar


while true
do
        echo $(date +%F%n%T)
        curl -1 https://www.baidu.com -H 'authToken:xxx' -s
        echo ''
        sleep 0.2
done

nohup ./curltest.sh >> curltest.log &
comment
