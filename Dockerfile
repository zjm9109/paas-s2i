FROM 139.219.239.226/library/java:8-jdk

ENV JAR_NAME bocloud.paas.s2i-0.0.1-SNAPSHOT.jar

ADD target/$JAR_NAME /opt

EXPOSE 8080
CMD ["java -jar /opt/$JAR_NAME"]
