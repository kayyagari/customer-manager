## Customer Manager
A SpringBoot based REST service.

## Building
Maven 3.x and JDK17 are required.
Checkout the project and then run `mvn clean install` to build.
To execute the service, run the command `java -jar customer-api/target/customer-api-1.0-SNAPSHOT.jar`from project's base directory.

## Design Rationale
1. Tried to keep things as simple and clear as possible (avoided abstraction layers wherever possible, e.g there are no DTOs)
2. Some patterns (e.g DAO) were added just for the sake of presenting how they can be used


## What is not working
I am still fiddling with making the Aspect trigger, (there is a clear understanding of how to get the auditing part work, but the aspect glue is getting in my way)

## What was not added
Javadoc was added where things are not obvious, rest of the places the code is self-explanatory.
