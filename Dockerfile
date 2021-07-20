FROM openjdk:8

LABEL maintainer="jscharf"

ADD target/application.jar    /usr/share/wits/application.jar
ADD target/lib/               /usr/share/wits/lib

ENTRYPOINT ["java", "-jar", "/usr/share/wits/application.jar"]
