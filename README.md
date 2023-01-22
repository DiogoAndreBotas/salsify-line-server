# Line Server REST API

## Building and running the service

The only requirement for running this service locally is [Docker](https://docs.docker.com/get-docker/). After Docker is up and running, building and running the web server is trivial. To do it, simply execute `build.sh`, and then `run.sh`.

## How does the system work?

This application runs a RESTful API that contains a single endpoint, `GET /lines/{lineIndex}`. Inside the web server is an immutable file that contains an N amount of lines, that can be queried at any time (provided that the service is up), using the GET endpoint.

It was required that the solution to read the file and fetch the desired line be efficient, and that a database could not be involved. An important aspect of the problem is the following statement: `Any given line will fit into memory`.

As such, the chosen solution uses Java Streams to accomplish the required efficiency of fetching the required line from a file that could potentially have gigabytes of lines. Using the [Files.lines(Path path)](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html#lines-java.nio.file.Path-) method, the lines of the file can be skipped (an index number of times), and then the first line of the Stream can be fetched. The Stream is populated lazily once it is consumed (occurs when calling the terminal operation `findFirst`).

Other considered solutions:
- Using a Database, which would involve deconstructing the file in key-value pairs (line index - > line), using a NoSQL database. This would involve an external system to depend on. To store the values themselves, it 's possible to either execute a script and load the entire data into the database, or cache each line, once it is requested for the first time. Although the latter implementation would require checking if the line is already stored in the database, regardless if it is. And if it is not, the data would either be stored or fetched.
- Using an HashMap to store the lines when the system boots, which would do the same as above but wouldn't rely on an external system. This approach would favour access time of the lines (O(1), without collisions) but would involve storing the entire content of the file in a data structure. With the file potentially having gigabytes of size, it would not be favourable to store it all in memory, or we risk having `OutOfMemoryExceptions`.

## How will the system perform with files of different sizes?

The bigger the index, the more time it will take to fetch the line. With a 1GB file, fetching the last line took approximately 3 seconds. The more elements the Stream needs to skip, the longer it will take to reach the desired line. However, the tradeoff is that only the line will be stored in memory, while being fetched and returned to the user via HTTP. So it makes sense for that the time complexity is not the best.

While I could not test with 10GB and 100GB text files, the time to fetch a line from these files will scale depending on its index. The bigger the index, the more time it will take for the response to be sent.

## How will the system perform with several users, accessing it at a certain time?

The file that is contained inside the web server is immutable, hence reading from it is thread-safe. This is reinforced by the concurrent tests run in [LinesApiConcurrentIntegrationTest.java](https://github.com/DiogoAndreBotas/salsify-line-server/blob/main/src/test/java/com/diogoandrebotas/salsifylineserver/LinesApiConcurrentIntegrationTest.java), which simulate five different threads fetching the exact same line via the GET endpoint. The logic of the controller and the service is stateless, meaning there can't be any race conditions between threads.

Spring Boot (using the `spring-boot-starter-web` dependency) supports simultaneous requests. By default, it automatically embeds a Tomcat servlet container, and supports a maximum of 200 threads ([code reference](https://github.com/spring-projects/spring-boot/blob/47516b50c39bd6ea924a1f6720ce6d4a71088651/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/web/ServerProperties.java#L814)). This value can be changed, and the ceiling there should be what the hardware dictates.

So, by default, the REST API serves a maximum of 200 concurrent users. This number can be changed, so let's say we have a maximum of 1000 threads. If we wanted to modify the system so it is scalable up to 10 thousand, or a 1 million users, we would have to change the architecture and introduce a load balance into the equation ([diagram](https://github.com/DiogoAndreBotas/salsify-line-server/blob/main/lines_system_with_load_balancer.png)). Several instances of the API would be running and the load balancer would redirect the user's requests to the instances with less load.

## Choice of frameworks, libraries and tools

## Time spent on the exercise

Two afternoons.

## In case of unlimited time

## Code critiques
