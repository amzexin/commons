# 获取hostname和IP
if [ ${HOSTNAME} = ""]; then
    HOSTNAME="localhost"
fi
ip=$(ifconfig -a | grep inet | grep -v 127.0.0.1 | grep -v inet6 | awk '{print $2}' | tr -d "addr:")

# 获取jar当前所在目录
jar_directory=`pwd`

# 企业微信机器人API: https://developer.work.weixin.qq.com/document/path/91770
webhook_key="xxxx"
curl "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=${webhook_key}" \
    -H "Content-Type: application/json" \
    -d \
    "{
    	\"msgtype\": \"markdown\",
    	\"markdown\": {
        	\"content\": \"# <font color="warning">服务发生OOM，请尽快处理</font> \n> 触发时间: $(date '+%Y-%m-%d %H:%M:%S') \n> 设备名称: ${HOSTNAME}(${ip}) \n> 所在目录: ${jar_directory} \"
    	}
    }"
