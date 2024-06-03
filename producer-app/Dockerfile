# see stackoverflow post: https://stackoverflow.com/a/59022743
FROM gradle:8.7.0-jdk21 as cache
# Gradle plugin and dependency cache is located in $GRADLE_USER_HOME/caches
# GRADLE_USER_HOME must be set to something different than /home/gradle/.gradle
# /home/gradle/.gradle in parent Gradle Docker image is defined as volume and is erased after each image layer.
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME /home/gradle/cache_home
# copy the (build|settings).gradle.kts file: to invalidate the cache when this file changes
COPY producer-app/build.gradle.kts /home/gradle/java-code/
COPY ../settings.gradle.kts /home/gradle/java-code/
WORKDIR /home/gradle/java-code
RUN gradle clean build --no-daemon -i --stacktrace

FROM gradle:8.7.0-jdk21 as builder
# copy the dependencies from the cache stage to avoid downloading the dependencies again
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY . /usr/src/java-code/
WORKDIR /usr/src/java-code
RUN gradle bootJar --no-daemon -i --stacktrace

FROM eclipse-temurin:21 as jre
RUN mkdir /app
COPY --from=builder /usr/src/java-code/producer-app/build/libs/producer-app.jar /app/
ENTRYPOINT ["java","-jar","/app/producer-app.jar","--spring.kafka.bootstrap-servers=kafka:9092"]
