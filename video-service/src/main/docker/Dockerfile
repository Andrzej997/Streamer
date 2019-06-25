FROM java:8
VOLUME /tmp
ADD video-service-1.0.jar app.jar
RUN echo "deb [check-valid-until=no] http://cdn-fastly.deb.debian.org/debian jessie main" > /etc/apt/sources.list.d/jessie.list
RUN echo "deb [check-valid-until=no] http://archive.debian.org/debian jessie-backports main" > /etc/apt/sources.list.d/jessie-backports.list
RUN sed -i '/deb http:\/\/deb.debian.org\/debian jessie-updates main/d' /etc/apt/sources.list
RUN apt-get -o Acquire::Check-Valid-Until=false update
RUN bash -c 'touch /app.jar' && \
    apt-get install -y ffmpeg && \
    apt-get clean
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dffmpeg.exec=ffmpeg","-jar","/app.jar"]
