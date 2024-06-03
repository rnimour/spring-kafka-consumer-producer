package com.rnimour.messaging.kafka

import jakarta.annotation.PostConstruct
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@ConditionalOnProperty("rnimour.produce", havingValue = "true")
@Configuration
open class KafkaProducerConfig {

    companion object {
        const val TOPIC = "my-spring-topic"
    }

    @Value(value = "\${spring.kafka.bootstrap-servers:localhost\\:9092}") // escape the colon (as it's Spring's default value separator)
    lateinit var bootstrapAddress: String

    @Bean
    open fun kafkaAdmin(): KafkaAdmin = KafkaAdmin(
        mutableMapOf<String, Any>(
            AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
        )
    )

    @Bean
    open fun mySpringTopic(): NewTopic = NewTopic(TOPIC, 1, 1)

    @Bean
    open fun producerFactory(kafkaAdmin: KafkaAdmin): ProducerFactory<String, String> =
        DefaultKafkaProducerFactory(
            mutableMapOf<String, Any>(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
            )
        )

    @Bean
    open fun kafkaTemplate(producerFactory: ProducerFactory<String, String>): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory)
    }

    @PostConstruct
    fun constructionComplete() = println("This app is a producer! bootstrapAddress: $bootstrapAddress")
}