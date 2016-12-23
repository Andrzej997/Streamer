FROM java:8
VOLUME /tmp
ADD video-service-1.0.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 9008
ENV SPRING_PROFILES_ACTIVE docker
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","/app.jar"]