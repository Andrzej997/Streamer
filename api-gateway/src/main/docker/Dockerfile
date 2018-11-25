FROM java:8
VOLUME /tmp
ADD api-gateway-1.0.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 10000
EXPOSE 5007
ENV SPRING_PROFILES_ACTIVE docker
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Xdebug","-Xnoagent","-Xrunjdwp:transport=dt_socket,address=5007,server=y,suspend=n","-Dspring.profiles.active=docker","-jar","/app.jar"]