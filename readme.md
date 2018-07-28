# Transfer Service
  
### Functionality
Create accounts with value.  
Transfer funds between accounts.
  
### Prerequisites
 - Port 8888 is free  
 - Java 8 is installed  
  
### Frameworks  

 - OpenApi - Used to define how the api looks.
 - Spring boot/data  - Dependenvy management
 - jpa/jta - Database access and transaction management
 - lombok - To avoid writing lots of border plate code.
 - springfox - Displaying documentation.
 - h2 - at-hoc database engine to simplify running the application simpler.
 
### Running the service
To compile and run this service:  
 `./mvnw spring-boot:run`  
Api's are available at http://localhost:8888  
They can be tested live on the website.  

### Database
The service itself is delivered with an empty database.  
To see what is in the database:  
  
http://localhost:8888/h2-console/  
Login with the following details:  
- driver class: `org.h2.Driver`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `<empty>`