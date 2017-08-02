FROM 139.219.239.226/library/java:8-jdk

COPY ./source-to-image-v1.1.7-226afa1-linux-amd64.tar.gz /opt/
RUN tar zxvf /opt/source-to-image-v1.1.7-226afa1-linux-amd64.tar.gz -C /usr/bin

ADD target/bocloud.paas.s2i-0.0.1-SNAPSHOT.jar /opt

EXPOSE 8080
WORKDIR /opt

CMD ["java", "-jar", "bocloud.paas.s2i-0.0.1-SNAPSHOT.jar"]