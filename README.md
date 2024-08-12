# Document Management Application

Spring Boot application for Document Management

# Run application with Docker
To run the application follow the steps below:  
~~~sh 
./gradlew build
docker compose up --build
~~~

###### APIs can be called on following endpoint
1. http://localhost:8080/api/authors  
2. http://localhost:8080/api/documents  

# Run Test Cases
To run Test cases run the following command on root folder of application  
~~~sh
./gradlew test
~~~

# Test Result
![image](https://github.com/user-attachments/assets/69eef763-84e2-4bed-a912-e2ba707a4c48)

# API Documentation
For Local environment API document will be avaiable on following URL  
http://localhost:8080/swagger-ui/index.html



# Failover
If application does not run with above specified steps, then there is a chance that last minute changes related to RabbitMQ and Docker Compose might be the reason.  
So you can go to repository status before RabbitMQ using below URL.  
https://github.com/zeeshanhanif/spring-boot-document-management/tree/3c6f30503221d3b0c276bff0082c906abc7666ea  

Execute following command to run the application  
~~~sh
./gradlew bootRun 
~~~
