FROM openjdk:18-jdk-alpine3.15
RUN mkdir /app
COPY ./build/libs/upload_file-0.0.1-SNAPSHOT.jar /app
COPY ./upload/ /app/upload/
WORKDIR /app
ENV UPLOAD_DIRECTORY=/app/upload/
CMD ["java", "-jar", "upload_file-0.0.1-SNAPSHOT.jar"]

#docker exec -it <container-name> sh
#docker run --name file -v <host-path>:/app/upload/ -p8080:8080
