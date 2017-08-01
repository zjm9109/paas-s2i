FROM 139.219.239.226/library/java:8-jdk

ADD target/bocloud.paas.s2i-0.0.1-SNAPSHOT.jar /opt

EXPOSE 8080
CMD ["java -jar /opt/bocloud.paas.s2i-0.0.1-SNAPSHOT.jar"]
