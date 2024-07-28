## Installation

Prerequisites:
Have the java jdk 21 installed
Have maven installed

Package the app in a jar, from the phoneAPI directory

```bash
mvn clean package
```

Start the app by grabbing the jar file in the phoneAPI/target directory

```bash
java -jar target/rest-service-complete-0.0.1-SNAPSHOT.jar
```

The app will run under http://localhost:8080
No security is set so https will not work

## Automated tests

To run the automated tests, go under the karate-tests directory and run

```bash
mvn clean test
```

## Redis



## Improvements

Since time was limited to do the development I think there are a lot of improvements that could be made:
1. Error responses: currently my app only catch and return very generic errors, I would add different exception classes and an error handler that would display a message more specific to the error encountered.
2. I would set up some configuration file to be able to edit things such as the port the app is running on and add a ssl certificate to support https for example.
3. Add unit testing to cover every flow of the code.
4. Add some automated tests and add more robust matching on the response to make sure we actually get the right response and not just test the http status or part of response body.
5. Add documentation (such as a swagger document) to explain the input parameters of the endpoint and possible responses.
6. Add code analysis tool such as SonaQube to the project to scan the code for programmatic/stylistics errors.
