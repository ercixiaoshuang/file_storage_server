# File Storage Server

## What is it

The File Storage Server makes a Java Spring boot application to easily upload/delete/list files on the server.

## How does it work

### Technologies Used

- [Java](https://www.oracle.com/java/technologies/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [docker](https://www.docker.com/)

### Repositories

|Repository | Information |
|--------|----------|
|[Main Repository - mkt_ifd_performance](https://git.rakuten-it.com/projects/ADMKTIFD/repos/mkt_ifd_performance/browse)| Main repository of the mkt_ifd_performance batch.|
|[Dependency - ultra_dashboard_UI_performance_test](https://git.rakuten-it.com/projects/ADMKTIFD/repos/mkt_ifd_performance/browse?at=refs%2Fheads%2Ffeature%2FTRAR-3692_prepare_test_data)| The module we are using to generate CV summary records|



### Start the Application 
Use Maven and run the following command in a terminal window. 
First, download this repository and go to ${file_storage_server} directory. As for target folder for file uploading/deletion/listing, please replace the setting in application.properties. Folder is setting there. 
```
    ./mvnw spring-boot:run
```
You should see output similar to the following:
```    
    2022-03-02 01:16:02.879  INFO 8296 --- [           main] c.w.challenge.FileStorageApplication     : Started FileStorageApplication in 1.15 seconds (JVM running for 1.393)
```

### Run File Storage Server with CLI
Now run the service with curl (in a separate terminal window), by running the following command (shown with its output):

- Command to Upload file to server (please replace the file path)
```shell
curl -XPOST -k 'http://localhost:8080/files' -F file=@'/Users/xiaoshuang.xu/Desktop/test.png'

# Sample of output
{"message":"Uploaded the file successfully: test.png"}
```

- Command to List all files on server
```shell
curl --location --request GET 'http://localhost:8080/files'

# Sample of output 
[{"name":"/Users/xiaoshuang.xu/git/file_storage_server/test.png","url":"http://localhost:8080/files/test.png"}]
```

- Command to delete files on server
```shell
curl --location --request DELETE 'http://localhost:8080/files/test.png'

# Sample of output
{"message":"Deleted the file successfully: test.png"}
```

### Run File Storage Server on different server
Please overwrite the following property files for your local/stg/dev/production server.
```
    LOCAL: application-local.properties
    STG: application-stg.properties
    DEV: application-dev.properties
    PROD: application-prod.properties
```
### Dockerizing File Storage Server application
Create Dockerfile for File Storage Server Spring Boot App(don't use docker-compose )

```
    FROM openjdk:8-jdk-alpine
    RUN mkdir -p /app
    WORKDIR /app
    ARG JAR_FILE=target/*.jar
    COPY ${JAR_FILE} app.jar
    ENTRYPOINT ["java","-jar","app.jar"]
```

Build an dockerimage with the following command:
```
    docker build -t filestorage/file-storage-server-docker .
```

Run File Storage Server by running the following command:
```
    docker run -p 8080:8080 filestorage/file-storage-server-docker
```

You should see output similar to the following:
```    
    2022-03-02 01:16:02.879  INFO 8296 --- [           main] c.w.challenge.FileStorageApplication     : Started FileStorageApplication in 1.15 seconds (JVM running for 1.393)
```