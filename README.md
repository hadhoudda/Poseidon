
# Poseidon
# Poseidon Trading Application

Poseidon is a trading application designed to manage and monitor trading activities. It offers features such as bid management, curve point handling, ratings, rule configuration, trade tracking, and user management.

## Technical:

1. Spring Boot 3.1.0
2. Spring Security
3. Java 21
4. Thymeleaf
5. Bootstrap


## Table of Contents
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Database Setup](#database-setup)
- [Running Tests](#running-tests)


## Installation

To install the application, follow these steps:

1. Clone the repository:
    ```sh
    git clone https://github.com/hadhoudda/Poseidon
    ```
2. Navigate to the project directory:
    ```sh
    cd poseidon
    ```
3. Build the project using Maven:
    ```sh
    mvn clean install
    ```

## Configuration

Configure the application by modifying the `application.properties` and `application-test.properties` files located in the `src/main/resources` directory. These files contain settings for database connections, logging, and other configurations.

## Usage

To run the application, use the following command:

```sh
mvn spring-boot:run
```
The application will be available at http://localhost:8080.

## Database Setup
Create database with name "demo" as configuration in application.properties 

```sh
spring.datasource.url=jdbc:mysql://localhost:3306/demo?serverTimezone=UTC

```
Ensure your database server is running and accessible.

## Running Tests
To run the tests, use the following command:
```sh
mvn test
```
