# 将Maven项目打成jar包
mvn clean package -Dmaven.test.skip=true

#cp target/spring-boot-demo-0.0.1-SNAPSHOT.jar tmp/spring-boot-demo-0.0.1-SNAPSHOT.jar

cp jar-control.sh target/jar-control.sh

cp Dockerfile target/Dockerfile

cd target

docker build -t bootdemo .

# docker run -d -p 8080:8080 --name myboot bootdemo