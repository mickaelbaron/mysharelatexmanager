# MySharelatexManager Back-end

MySharelatexManager back-end is powered by Java 11 with the [MicroProfile](https://microprofile.io/) specifications and [KumuluzEE](https://ee.kumuluz.com/) implementation (JAX-RS and CDI). The unit tests are using JUnit, CDIUnit, Mockito and Testcontainers.

## Software requirements

* Java >= 11
* Maven

## Compile

```
$ mvn clean package
```

## Run

```
$ cd 
$ java -cp 'target/classes:target/dependency/*' com.kumuluz.ee.EeApplication
```