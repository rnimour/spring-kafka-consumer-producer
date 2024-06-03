package com.rnimour.messaging.kafka

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Component
class MyKafkaProducer(val kafkaTemplate: KafkaTemplate<String, String>) {

    fun sendMessage(topic: String, message: String) {
        log(message)

        val future = kafkaTemplate.send(topic, message)
        future.whenComplete(handleCompletion(message))
    }

    private fun log(message: String) {
        val now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"))
        println("$now | Received <$message>. sending to Kafka")
    }

    private fun handleCompletion(message: String) = { result: SendResult<String, String>?, ex: Throwable? ->
        if (ex == null) {
            println("@offset ${result?.recordMetadata?.offset()}, sent message <$message>")
        } else {
            println("could not send message <$message> because of ${ex.message}")
        }
    }
}