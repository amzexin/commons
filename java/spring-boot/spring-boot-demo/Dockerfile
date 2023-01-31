FROM centos:centos7

# 安装jdk
RUN yum install java-1.8.0-openjdk.x86_64 -y

# 把项目拷贝到镜像中
COPY spring-boot-demo-0.0.1-SNAPSHOT.jar /home/

# 把项目启动脚本拷贝到镜像中
COPY jar-control.sh /home/

# 要暴露的端口
EXPOSE 8080

WORKDIR /home

CMD sh jar-control.sh