FROM java:8
VOLUME /tmp
ADD video-service-1.0.jar app.jar
RUN bash -c 'touch /app.jar' && \
    echo deb http://ftp.uk.debian.org/debian jessie-backports main >> /etc/apt/sources.list && \
    apt-get update && \
    apt-get install -y ffmpeg && \
    apt-get clean
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dffmpeg.exec=ffmpeg","-jar","/app.jar"]