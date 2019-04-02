FROM java:8
VOLUME /tmp
ADD image-service-1.0.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 5005
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Xdebug","-Xnoagent","-Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=n","-jar","/app.jar"]
