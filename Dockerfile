FROM 139.219.239.226/library/java:8-jdk

COPY ./source-to-image-v1.1.7-226afa1-linux-amd64.tar.gz /opt/
RUN tar zxvf /opt/source-to-image-v1.1.7-226afa1-linux-amd64.tar.gz -C /usr/bin

RUN mkdir -p /opt/workspace /opt/s2i_home/shell/
COPY target/bocloud.paas.s2i-0.0.1-SNAPSHOT.jar /opt/workspace
COPY ./shell/ /opt/s2i_home/shell/

EXPOSE 8080
WORKDIR /opt/workspace

CMD ["java", "-jar", "bocloud.paas.s2i-0.0.1-SNAPSHOT.jar"]