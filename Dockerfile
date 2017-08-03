FROM 139.219.239.226/library/java:8-jdk

# 容器中没有解析github.com
RUN echo "192.30.255.112  github.com" >> /etc/hosts

COPY ./source-to-image-v1.1.7-226afa1-linux-amd64.tar.gz /opt/
RUN tar zxvf /opt/source-to-image-v1.1.7-226afa1-linux-amd64.tar.gz -C /usr/bin

COPY target/bocloud.paas.s2i-0.0.1-SNAPSHOT.jar /opt/workspace/
COPY shell/ /opt/s2i_shell/

EXPOSE 8080
WORKDIR /opt/workspace

CMD ["java", "-jar", "bocloud.paas.s2i-0.0.1-SNAPSHOT.jar"]