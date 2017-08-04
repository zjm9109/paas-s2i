FROM 139.219.239.226/library/java:8-jdk

COPY source-to-image-v1.1.7-226afa1-linux-amd64.tar.gz /opt/
RUN tar zxvf /opt/source-to-image-v1.1.7-226afa1-linux-amd64.tar.gz -C /usr/bin

COPY target/bocloud.paas.s2i-0.0.1-SNAPSHOT.jar /opt/workspace/
COPY shell/ /opt/s2i_shell/

COPY docker_build.sh /opt/

EXPOSE 8080
WORKDIR /opt/workspace

# 容器中没有解析git的域名
CMD echo "192.30.255.112  github.com" >> /etc/hosts && java -jar bocloud.paas.s2i-0.0.1-SNAPSHOT.jar