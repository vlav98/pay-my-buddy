# Getting Started with Spring Boot and Thymeleaf

Prerequisites:
- Java 17 or above installed (`java -version` to check)
- Maven or Gradle (build automation tools)

You can either set up the whole project, or you can simply build an executable jar and run the jar.

Set Up:
--------

### 1. Project Setup:

Clean compilation products: `mvn clean`

Compile: `mvn compile`

### 2. Run the Application:

Navigate to your project directory in the terminal and run:

```Bash
mvn spring-boot:run
```

Once started, the application should be available at:

     http://localhost:8080/


Building the executable jar :
---------

Build the package: `mvn clean package`

Run the jar: `java -jar pay-my-buddy-0.0.1-SNAPSHOT.jar`
