# Behavior-Driven Development (BDD) with Cucumber and Spring Boot

This project highlights how to write **Behavior-Driven Development (BDD)** test cases with **Cucumber** and **Spring Boot**. It contains the implementation for a REST Endpoint designed to fetch book details based on a given book ID. We have written two BDD test cases to encompass scenarios where we call the endpoint with both an existing book ID and a non-existent one.

### What is Behavior-Driven Development (BDD)?

---
Behavior-Driven Development is a software development approach that emerged from test-driven development (TDD) and domain-driven design (DDD) methodologies. BDD focuses on defining the behavior of software through examples written in natural language that can be understood by both technical and non-technical stakeholders.

### What is Cucumber?

---
Cucumber is a popular open-source testing framework used for behavior-driven development (BDD). It supports the implementation of BDD practices in software development by enabling collaboration among stakeholders and automating acceptance criteria in a human-readable format.

Official documentation is at https://cucumber.io/

### Prerequisites

---
Before running the application and test cases, make sure the following list is installed/configured on your machine:

* Java version 17 or above
* Maven
* PostgreSQL
* pgAdmin (optional)
* Postman (optional)

### Running the Application and Test Cases

---
1. Clone the project using the command `git clone https://github.com/ruviniramawickrama/demo-bdd-with-cucumber-and-spring-boot.git`
2. Go to the cloned project's root directory and open a command prompt
3. Build the project using the command `mvn clean install`
4. To run the application:
   - Run the project using the command `mvn spring-boot:run`
   - Use the Postman collection `DemoBddWithCucumber.postman_collection.json` to invoke the REST end points (`\src\main\resources\DemoBddWithCucumber.postman_collection.json`)
5. To run the test cases:
   - Execute the test cases using the command `mvn clean test`


### Application Details - API Implementation

---
`application.yml`
- Contains the properties related to database connection (Postgres), JPA and SQL.

`schema.sql` and `data.sql`
- Contains DDL and DML SQL scripts to create and insert data into the Book table.
- When the application starts, Spring Boot automatically runs the contents of these files based on the `sql` and `jpa` properties mentioned in the `application.yml` file.

`BookController.java`
- REST Controller which contains `getBook` end point to retrieve Book details for the provided book ID.

`BookService.java`
- Implemented by `BookServiceImpl.java` which is responsible for communicating with the repository class to retrieve Book details.

`BookRepository.java` 
- Implements Spring Data JpaRepository which provides ready-made methods to communicate with the database to retrieve Book details. It uses `Book.java` as the entity which maps with the respective database table.

### Application Details - BDD Tests Implementation

---

`book.feature`
- Contains human-readable scenarios (written in Gherkin language) related to Get Book endpoint.
- The first two scenarios handle individual cases of invoking the endpoint with an existing book ID and a non-existing book ID.
- The feature file also includes an example of a **Scenario Outline** that combines the first two scenarios. A Scenario Outline serves as a template for defining multiple scenarios that share a similar structure but may have different inputs or data sets. This approach allows you to define a scenario once and then parameterize it with various values, resulting in more efficient and concise specification of test cases.

`BookSteps.java`
- Contains the related Java implementation of each step defined in `book.feature` file.

`CucumberConfiguration.java`
- Contains the two annotations `@CucumberContextConfiguration` and `@SpringBootTest`.
- `@CucumberContextConfiguration` - When you run Cucumber tests in a Spring Boot application, you might want to load Spring application context and have access to Spring beans for your tests. The `@CucumberContextConfiguration` annotation allows you to specify the configuration classes that define the Spring context to be loaded for the test.
- `@SpringBootTest` - Tells Spring Boot to bootstrap the entire application context, enabling full end-to-end testing by loading all the beans and configurations associated with the application. If your Spring Boot application includes a web server (like Tomcat) and you are testing HTTP endpoints, `@SpringBootTest` starts an embedded server during testing, allowing you to send requests and receive responses as if the application were running in a live environment.

`CucumberRunnerTest.java`
- Contains the two annotations `@RunWith` and `@CucumberOptions`.
- `@RunWith` - Used to specify the test runner class that will be used to execute the tests.
- `@CucumberOptions` - Used to specify Cucumber related options such as location of feature files and their related steps files.
