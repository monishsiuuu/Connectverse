FROM amazoncorretto:21-alpine

WORKDIR /usr/app

LABEL "com.gmc.navin3d"="smnavin65@gmail.com"
LABEL version="0.1"

COPY ./target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app.jar" ]
