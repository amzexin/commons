jar_name="commons-test-jar-with-dependencies.jar"
deploy_path="../../../../../../../../../target"

# JVM 参数
java_opts=" "
java_opts=${java_opts}" -Xms2m"                      # 最小堆内存, 默认是服务器的1/64
java_opts=${java_opts}" -Xmx12m"                     # 最大堆内存, 默认是服务器的1/4
java_opts=${java_opts}" -XX:+ExitOnOutOfMemoryError" # OOM后，应用程序直接退出
# -XX:+HeapDumpOnOutOfMemoryError OOM后，生成dump文件
# -XX:HeapDumpPath 指定dump文件的生成路径，若不指定则默认在进程的工作目录生成dump文件；还可以指定dump文件的名称
# -Xloggc 指定gc日志文件路径（包括文件名）
# -XX:OnOutOfMemoryError 可以指定shell命令、shell脚本、其他脚本等，在OOM时执行一些额外的操作（实践证明，是先产生dump文件，再执行配置）

cd ${deploy_path}
java ${java_opts} -jar ${jar_name}
