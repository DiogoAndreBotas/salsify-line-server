# Line Server REST API

## Building and running the service

The only requirement for running this service locally is [Docker](https://docs.docker.com/get-docker/). After Docker is installed, building and running the web server is trivial. To do it, simply execute `build.sh`, and then `run.sh`.

## How does the system work?

This application runs a RESTful API that exposes a single endpoint, `GET /lines/{lineIndex}`. Inside the web server is an immutable file that contains an N amount of lines, that can be queried at any time (provided that the service is up), using the GET endpoint.

It was required that the solution to read the file and fetch the desired line be efficient, and that a database could not be involved. An important aspect of the problem is the following statement: `Any given line will fit into memory`.

As such, the chosen solution uses `Java Streams` to accomplish the required efficiency of fetching the line from a file that could potentially have gigabytes of data. Using the [Files.lines(Path path)](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html#lines-java.nio.file.Path-) method, the lines of the file can be skipped (an index number of times), and then the first line of the Stream can be fetched. The Stream is populated lazily once it is consumed (which occurs when calling the terminal operation `findFirst`). Using this solution, only the fetched line will be stored in memory.

Other considered solutions:
- Using a database, which would involve deconstructing the file in key-value pairs (line index -> line), using a NoSQL database. This would involve an external system to depend on. To store the values themselves, it's possible to either execute a script and load the entire data into the database, or cache each line, once it is requested for the first time. The latter implementation would require checking if the line is already stored in the database, regardless if it is. And if it is not, the data would either be stored or fetched, meaning two calls to the database would be done per request.
- Using an HashMap to store the lines when the system boots, which would do the same as above but wouldn't rely on an external system. This approach would favour access time of the lines (O(1), without hash collisions) but would involve storing the entire content of the file in a data structure. With the file potentially having gigabytes of size, it would not be favourable to store it all in memory, or we risk having `OutOfMemoryExceptions`.

## How will the system perform with files of different sizes?

The more elements the Stream needs to skip, the longer it will take to reach the desired line. With a 1GB file, fetching the last line took approximately 3 seconds. Fetching one of the first lines takes around 5 milliseconds. The tradeoff between space and time complexity is that only the line will be stored in memory, due to the contents of the Stream only being lazily loaded once the terminal operation is executed.

While I could not test with 10GB and 100GB text files, the time to fetch a line from these files will scale depending on its index. The bigger the index, the more time it will take for the line to be fetched.

## How will the system perform with several concurrent users?

The file that is contained inside the web server is immutable, hence reading from it is thread-safe. This is reinforced by the concurrent tests run in [LinesApiConcurrentIntegrationTest.java](https://github.com/DiogoAndreBotas/salsify-line-server/blob/main/src/test/java/com/diogoandrebotas/salsifylineserver/LinesApiConcurrentIntegrationTest.java), which simulate five different threads fetching the exact same line via the GET endpoint. The logic of the controller and the service is stateless, meaning there can't be any race conditions between threads.

Spring Boot (using the `spring-boot-starter-web` dependency) supports simultaneous requests. By default, it automatically embeds a Tomcat servlet container, and supports a maximum of 200 threads ([code reference](https://github.com/spring-projects/spring-boot/blob/47516b50c39bd6ea924a1f6720ce6d4a71088651/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/web/ServerProperties.java#L814)). This value can be changed, and the ceiling there should be what the hardware dictates.

So, by default, the REST API serves a maximum of 200 concurrent users. This number can be changed, so let's say we have a maximum of 1000 threads. If we wanted to modify the system so it is scalable up to 10 thousand, or a 1 million users, we would have to change the architecture and introduce a load balancer into the equation (see diagram at the end of the file). Several instances of the API would be running and the load balancer would redirect the user's requests to the instances with less load.

## Choice of frameworks, libraries and tools

- Java (19) -> The programming language I'm most comfortable with.
- Spring -> Application framework for building web applications in Java. This has way too many features to describe here. 
- JUnit -> Testing framework, allows for several annotations, contains assert methods, among other features.
- Mockito -> Testing framework that allows mocking of objects, in order to unit test the application. In this case, TDD (Test-Driven Development) was used.
- Gradle -> Build automation tool, it was Gradle VS Maven and I have more experience with Gradle.
- Docker -> Containerize application in order to build and run it in any OS.
- Insomnia -> Integration test REST API.
- Excalidraw -> Design high-level architecture diagrams.

## Time spent on the exercise

About 8 hours, spread out over a weekend.

## In case of unlimited time

- Add Load Balancer component.
- Experiment with the database approach, and check difference of space and time measurements.
- Add continuous integration to repository (static code analysis, automated tests, commit message analysis, dependency checks, etc).
- Deploy application into the cloud, and add continuous deployment to commits merged into the `main` branch.

## Code critiques

- The try catch in [LinesController](https://github.com/DiogoAndreBotas/salsify-line-server/blob/main/src/main/java/com/diogoandrebotas/salsifylineserver/controller/LinesController.java#L23) can be removed. Exceptions can be treated using a global ExceptionHandler ([documentation reference](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc)).

## Architecture diagrams

Using single instance of the web server:

![Untitled-2023-01-21-1523](https://user-images.githubusercontent.com/22375850/213944782-8de214bc-1b10-450b-8968-1d70a1dbb893.png)

Using several instances of web servers and a load balancer component:

![image](https://user-images.githubusercontent.com/22375850/213944801-0d4868b5-e3bd-479d-b873-9c25c81df17c.png)
