This is a tiny Spring Boot project that reads messages to Kafka running at `localhost:9092`.

The app will read messages from topic `my-spring-topic`, and print them to the console. 
By default, it is assigned to group `default-group`, but this can be changed by giving argument `rnimour.group=<your_group>`.

This app can also be run in a Docker container, see `Dockerfile`. Note that the property 
`--spring.kafka.bootstrap-servers=kafka:9092` is set to tell the rnimour-kafka-consumer to communicate with the `kafka` service.
The app will also communicate with Kafka running in another container. See `compose.yaml`.
With this file, `docker compose` runs Apache Zookeeper to manage the Apache Kafka instance that it also runs, and it runs the `rnimour-kafka-consumer`.

To run this project, simply run `docker compose up --build` in the project's root directory.
