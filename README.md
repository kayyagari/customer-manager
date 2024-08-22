[![Java CI with Maven](https://github.com/kayyagari/customer-manager/actions/workflows/ci.yml/badge.svg)](https://github.com/kayyagari/customer-manager/actions/workflows/ci.yml)
## Customer Manager
A SpringBoot based REST service.

## Building
Maven 3.x and JDK17 are required.
Checkout the project and then run `mvn clean install` to build.
To execute the service, run the command `java -jar customer-api/target/customer-api-1.0-SNAPSHOT.jar`from project's base directory.

## Design Rationale
1. Tried to keep things as simple and clear as possible (avoided abstraction layers wherever possible, e.g there are no DTOs)
2. Some patterns (e.g DAO) were added just for the sake of presenting how they can be used
3. Audit logging has been implemented in asynchronous mode instead of AspectJ, this results in some verbose code but gives more control and it stays out of the main thread of execution)
4. Tests have been added for both DB layer and REST API layers. The test code accross various XXXResourceTest classes contains similar looking code but it was done to keep them individually testable
5. Javadoc was added where things are not obvious, rest of the places the code is self-explanatory
