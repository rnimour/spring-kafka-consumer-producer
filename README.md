This is a tiny Spring Boot project that reads messages to Kafka running at `localhost:9092`.

The app will read messages from topic `my-spring-topic`, and print them to the console. 
By default, it is assigned to group `default-group`, but this can be changed by giving argument `rnimour.group=<your_group>`.
