# 当前目录
current_directory=$(pwd)

# 先切换到jar所在目录
cd ../../../../../../../../../target

# 指定jar文件名和所在目录
jar_directory=$(pwd)
jar_name="commons-test-jar-with-dependencies.jar"
jar_path=${jar_directory}/${jar_name}

# 指定dump文件名和所在目录
dump_file_directory="${jar_directory}/dump"
mkdir -p ${dump_file_directory}
dump_file_path="${dump_file_directory}/$(date '+%Y%m%d-%H%M%S').hprof"

# 指定gc文件名和所在目录
gc_log_directory="${jar_directory}/gclog"
mkdir -p ${gc_log_directory}
gc_log_path="${gc_log_directory}/gc.log"

# 指定OOM后要执行的shell脚本
shell_on_oom_path="${current_directory}/script-on-oom.sh"

# 设置JVM参数
java_opts=""
java_opts="${java_opts} -Xms1g" # 堆区初始值, 默认是服务器的1/64
java_opts="${java_opts} -Xmx1g" # 堆区最大值, 默认是服务器的1/4
#java_opts="${java_opts} -XX:+ExitOnOutOfMemoryError"                 # OOM后，应用程序直接退出
#java_opts="${java_opts} -XX:+HeapDumpOnOutOfMemoryError"    # OOM后，生成dump文件。注：进程不退出时只会生成一次，详见 https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6448456
#java_opts="${java_opts} -XX:HeapDumpPath=${dump_file_path}" # 指定dump文件的生成路径，若不指定则默认在进程的工作目录生成dump文件；还可以指定dump文件的名称
#java_opts="${java_opts} -Xlog:gc:${gc_log_path}"                     # 指定gc日志文件路径（包括文件名）
#java_opts="${java_opts} -XX:OnOutOfMemoryError=${shell_on_oom_path}" # 可以指定shell命令、shell脚本、其他脚本等，在OOM时执行一些额外的操作（实践证明，是先产生dump文件，再执行配置）
java_opts="${java_opts} -XX:+UseG1GC"   # 使用G1收集器
java_opts="${java_opts} -XX:NewRatio=2" # JVM新生代（2个Survivor区和Eden区 ）与老年代（不包括永久区）的堆空间比值，表示新生代：老年代=1：2

# 启动项目
#java ${java_opts} -jar ${jar_path}
nohup java ${java_opts} -jar ${jar_path} >/dev/null 2>&1 &
