# Fleet manager microservice   
This microservice is responsible to manage fleet of container carrier ships. It exposes simple REST API to support getting ships and fleets, and start and stop simulator.

## Pre-Requisites

* [Maven](https://maven.apache.org/install.html)
* Java 8: Any compliant JVM should work.
  * [Java 8 JDK from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
  * [Java 8 JDK from IBM (AIX, Linux, z/OS, IBM i)](http://www.ibm.com/developerworks/java/jdk/),
    or [Download a Liberty server package](https://developer.ibm.com/assets/wasdev/#filter/assetTypeFilters=PRODUCT)
    that contains the IBM JDK (Windows, Linux)
* We used Eclipse IDE


## The model
A fleet will have 1 to many ship. Fleet has id and name. Ship has ID, name, status, position, port and type.

## Code
The base of the project is created using IBM Microclimate using microprofile / Java EE template deployable in WebSphere Liberty. Once the code is generated we apply a Test Driven Development approach.

### User stories

#### Fleet Operations

* Support getting the list of existing list. => Starting from the test the unit test is under 


## Deployment

### Configuration

The application is configured to provide JAX-RS REST capabilities, JNDI, JSON parsing and Contexts and Dependency Injection (CDI).
These capabilities are provided through dependencies in the pom.xml file and Liberty features enabled in the server config file found in `src/main/liberty/config/server.xml`.

### Locally

To build and run the application:

1. `mvn install`
1. `mvn liberty:run-server`


To run the application in Docker use the Docker file called `Dockerfile`. If you do not want to install Maven locally you can use `Dockerfile-tools` to build a container with Maven installed.


### On IBM Cloud 
To deploy this application to IBM Cloud using a toolchain click the **Create Toolchain** button.
[![Create Toolchain](https://console.ng.bluemix.net/devops/graphics/create_toolchain_button.png)](https://console.ng.bluemix.net/devops/setup/deploy/)


