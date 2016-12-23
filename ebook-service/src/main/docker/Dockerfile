FROM java:8
VOLUME /tmp
ADD ebook-service-1.0.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 9002
ENV SPRING_PROFILES_ACTIVE docker
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","/app.jar"]