# Document Management Application

Spring Boot application for Document Management

# Run application
To start application run the following command on root folder of application

./gradlew bootRun

# Build and Run Docker Container
Run following command in root folder
docker build -t document-management .
docker run -p 8080:8080 document-management

APIs can be called on following endpoint
http://localhost:8080/api/authors
http://localhost:8080/api/documents

# Run Test Cases
To run Test cases run the following command on root folder of application

./gradlew test

# Test Result
![image](https://github.com/user-attachments/assets/69eef763-84e2-4bed-a912-e2ba707a4c48)

# API Documentation
For Local environment API document will be avaiable on following URL

http://localhost:8080/swagger-ui/index.html
