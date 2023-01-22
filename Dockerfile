FROM amazoncorretto:19 as build
LABEL maintainer='Diogo André Botas'

WORKDIR /workspace/app
COPY . /workspace/app
RUN ./gradlew build --no-daemon

FROM amazoncorretto:19 as production
WORKDIR /app
VOLUME /tmp
COPY --from=build /workspace/app/build/libs/lines-web-api.jar /app/
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/lines-web-api.jar"]
