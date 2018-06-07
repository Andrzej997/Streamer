FROM java:8
VOLUME /tmp
ADD video-service-1.0.jar app.jar
RUN bash -c 'touch /app.jar' && \
    echo deb http://ftp.uk.debian.org/debian jessie-backports main >> /etc/apt/sources.list && \
    apt-get update && \
    apt-get install -y ffmpeg && \
    apt-get clean
EXPOSE 9008
EXPOSE 5005
ENV SPRING_PROFILES_ACTIVE docker
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Xdebug","-Xnoagent","-Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=n","-Dspring.profiles.active=docker","-Dffmpeg.exec=ffmpeg","-jar","/app.jar"]