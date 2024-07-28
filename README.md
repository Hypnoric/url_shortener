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

## Technical choices

For the API app, I chose to use java Spring Boot as it is a framework I already have experience in and templates are available to start a project rapidly, the other framework that came to my mind to do this kind of project would have been Python fastAPI but since I have more profesionnal experience with Java SpringBoot I choose to stay with that one since familiarity with the framework would be more important here since I was asked to only spend 2 hours on development.

I also found the libphonenumber by google which is a Java library with a lot of useful utilities for this project and could be easily integrated into it, I lookes at the documentation to take note of which functions provided by that library would be useful to do this project before starting the development.

For automated testing, I chose karate-automation, which is the framework we use regularly at my current job. I like and and it is pretty easy to use. It is also indepedant from the framework used for the API so it is very easy to make tests at the start to do some test driven development. It also support native javascript functions which can be useful.

## How I would deploy this app to production

After getting the jar file fron the installation instructions,
I will be assuming we already have some environment set up to use for this such as some AWS virtual machines or a physical server, otherwise I would probably create some AWS virtual machine to host the app.
Assuming there is a testing environment, I would do the following on the test environment first
Make sure the web server (apache for example) is configured correctly to allow access the the url and ports used by the app.
Make sure any required environment variables are set on the test box
Do the same for any framework/software needed, such as java for exmaple
Copy the jar file on the environment, make sure it is executable and start the app.
Run all the automated tests, and any other existing tests for regression testing.
Once this is done and any test failures have been addressed, repeat the process on the production environment.

## Assumptions

In the requirement: "phones can spaces between country, area code and local phone number" there seems to be a word missing *can HAVE*
It was not clear to me if multiple spaces could be between 2 parts of the number (ex: 123     454     45454) so I assumed only one space is accepted.

For number validation, even if libphonenumber supports number validation it still didn't correspond to the exact requirement asked so I added an additional regular expression check.

Since E.164 doesn't dictate how many numbers are in the area code or the local phone number I went with the following:
* Country code is 1 to 3 numbers and cannot start by 0
* Area code is 1 to 3 numbers
* Local phone is 1 to 8 numbers

As it was asked to limit the time spent on development, I prioritised successful reponses to error reponses, so my returned error do not reflect the expected behavior, like returning the erreneous field.

I also assumed the alpha-2 country code can still be passed to the endpoint even if a numeric country code is already in the phone number.

## Improvements

Since time was limited to do the development I think there are a lot of improvements that could be made:
1. Error responses: currently my app only catch and return very generic errors, I would add different exception classes and an error handler that would display a message more specific to the error encountered.
2. I would set up some configuration file to be able to edit things such as the port the app is running on and add a ssl certificate to support https for example.
3. Add unit testing to cover every flow of the code.
4. Add some automated tests and add more robust matching on the response to make sure we actually get the right response and not just test the http status or part of response body.
5. Add documentation (such as a swagger document) to explain the input parameters of the endpoint and possible responses.
6. Add code analysis tool such as SonaQube to the project to scan the code for programmatic/stylistics errors.
