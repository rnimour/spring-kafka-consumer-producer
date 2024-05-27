package com.rnimour.messaging.kafka

import com.rnimour.messaging.kafka.KafkaTopicConfig.Companion.TOPIC
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*

@Component
class MyKafkaConsumer {

    @KafkaListener(topics = [TOPIC], idIsGroup = false) // let the group come from consumerFactory
    fun listenToTOPIC(
        @Payload message: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        @Header(KafkaHeaders.RECEIVED_TIMESTAMP) receivedTimestamp: Long,
    ) {
        val receivedTime = SimpleDateFormat("HH:mm:ss.SSS").format(Date(receivedTimestamp))

        println("received: $receivedTime | part $partition | Message <$message>")
    }
}