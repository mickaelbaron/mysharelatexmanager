# MySharelatexManager Back-end

MySharelatexManager back-end is powered by Java 11 with the [MicroProfile](https://microprofile.io/) specifications and [Open Liberty](https://openliberty.io/) implementation (JAX-RS and CDI). The unit tests are using JUnit 5, Mockito and Testcontainers.

## Software requirements

- Java >= 11
- Maven

## Compile and run unit tests

```bash
mvn clean package
```

## Run

```bash
cd mysharelatexmanager-server
mvn clean liberty:run
```
