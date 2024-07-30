## Installation

Prerequisites:
* Have the java jdk 17 installed
* Have maven installed

Package the app in a jar, from the restservice directory

```bash
mvn clean package
```

Start the app by grabbing the jar file in the restservice/target directory.

```bash
java -jar target/rest-service-complete-0.0.1-SNAPSHOT.jar
```

The app will run under http://localhost:8080. 
No security is set so https will not work.

## Automated tests

From the restservice directory
```bash
mvn clean test
```

## Redis

Install docker and docker desktop. 
Make sure the docker deamon is running and the redis container started.

```bash
sudo dockerd 
sudo docker run -p 6379:6379 -it redis/redis-stack:latest
```


## Documentation

After running the service, go to http://localhost:8080/swagger-ui/index.html for documentation of the endpoints.
