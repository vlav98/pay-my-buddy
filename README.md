# Getting Started with Spring Boot and Thymeleaf

Prerequisites:
- Java 17 or above installed (`java -version` to check)
- Maven or Gradle (build automation tools)

Set Up:
--------

### 1. Project Setup:

Clean compilation products: `mvn clean`

Compile: `mvn compile`

4. Create a Thymeleaf Template:

Within the src/main/resources/templates directory, create a basic HTML file (e.g., index.html). This will be your Thymeleaf template.

Here's an example template:
```
HTML
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Spring Boot with Thymeleaf</title>
</head>
<body>
    <h1>Welcome to Spring Boot!</h1>
    <p>This is a simple Thymeleaf template.</p>
</body>
</html>
```

5. Create a Spring Boot Application Class:

In src/main/java/com.yourcompany, create a class extending SpringBootApplication. This is your main application class.

```Java
@SpringBootApplication
public class YourApplicationName {

    public static void main(String[] args) {
        SpringApplication.run(YourApplicationName.class, args);
    }
}
```
6. Run the Application:

Navigate to your project directory in the terminal and run:

```Bash
mvn spring-boot:run
```

7. Access the Thymeleaf Template:

By default, Spring Boot serves static content from the resources/static directory. However, Thymeleaf templates are processed dynamically.

Spring Boot typically exposes Thymeleaf templates on the root path (/). Access your template using http://localhost:8080/ (or your specific port) in a web browser.

8. Thymeleaf Expressions:

Thymeleaf allows you to integrate dynamic content using expressions. You can include variables defined in your Spring Boot application or controller methods within your templates using ${variableName} syntax.

Building
--------

Clean compilation products:

     mvn clean

Compile:

     mvn compile

Run in a tomcat server:

     mvn tomcat7:run

Once started, the application should be available at:

     http://localhost:8080/
