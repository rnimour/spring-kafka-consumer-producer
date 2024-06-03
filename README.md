This project contains two tiny Spring Boot modules which send messages to or read messages from Kafka.

The producer-app exposes a port at `localhost:8080` and has a single POST endpoint `/messages`
which sends the body of the HTTP request (text/plain) to the topic `my-spring-topic`.

The consumer-app will read messages from topic `my-spring-topic`, and print them to the console. 
By default, it is assigned to group `default-group`, but this can be changed by giving argument `rnimour.group=<your_group>`.

These apps can also be run in a Docker container, see the `Dockerfile`s.\
Note that the property `--spring.kafka.bootstrap-servers=kafka:9092` is set 
to tell the rnimour-kafka-consumer to communicate with the `kafka` service.
The app will also communicate with Kafka running in another container. See `compose.yaml`.
With this file, `docker compose` runs Apache Zookeeper to manage the Apache Kafka instance that it also runs,
and it runs the `rnimour-kafka-consumer` and `rnimour-kafka-producer`.

To run this project, simply run `docker compose up --build` in the project's root directory.\
You can then send messages to the producer-app with your favorite HTTP request tool. For example:\
`curl -X POST -d "aap noot mies" localhost:8080/message`\
You can see the messages being consumed by the consumer-app in the console where you ran `docker compose up --build`.
