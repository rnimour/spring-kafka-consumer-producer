package com.rnimour.messaging.kafka

import jakarta.annotation.PostConstruct
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.KafkaAdmin

@EnableKafka
@ConditionalOnProperty("rnimour.consume", havingValue = "true", matchIfMissing = true)
@Configuration
open class KafkaConsumerConfig {

    companion object {
        const val TOPIC = "my-spring-topic" // topic must be a constant because it's used in an annotation
    }

    @Value(value = "\${rnimour.group:default-group}")
    lateinit var groupId: String

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
    open fun consumerFactory(): ConsumerFactory<String, String> =
        DefaultKafkaConsumerFactory(
            mutableMapOf<String, Any>(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
                ConsumerConfig.GROUP_ID_CONFIG to groupId,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            )
        )

    @Bean
    open fun kafkaListenerContainerFactory(consumerFactory: ConsumerFactory<String, String>): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory
        return factory
    }

    @PostConstruct
    fun constructionComplete() =
        println("This app is a consumer! groupId: $groupId, bootstrapAddress: $bootstrapAddress")
}