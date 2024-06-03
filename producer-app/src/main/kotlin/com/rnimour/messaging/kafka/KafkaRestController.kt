package com.rnimour.messaging.kafka

import com.rnimour.messaging.kafka.KafkaProducerConfig.Companion.TOPIC
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * Very simple REST controller for Kafka producer. It sends a message to the Kafka topic when a POST request is received.
 */
@RestController
class KafkaRestController(val kafkaProducer: MyKafkaProducer) {

    @PostMapping("/message")
    fun post(@RequestBody body: String): ResponseEntity<String> {

        kafkaProducer.sendMessage(TOPIC, body)

        return ResponseEntity.ok(body)
    }
}