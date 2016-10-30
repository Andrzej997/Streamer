FROM java:8
VOLUME /tmp
ADD image-service-0.1.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 9004
ENV SPRING_PROFILES_ACTIVE docker
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","/app.jar"]