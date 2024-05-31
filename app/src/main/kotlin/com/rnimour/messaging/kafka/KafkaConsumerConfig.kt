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

    @Value(value = "\${rnimour.group:default-group}")
    lateinit var groupId: String

    @Value(value = "\${spring.kafka.bootstrap-servers:localhost\\:9092}") // escape the colon (as it's Spring's default value separator)
    lateinit var bootstrapAddress: String

    companion object {
        const val TOPIC = "my-spring-topic" // topic must be a constant because it's used in an annotation
    }

    @Bean
    open fun kafkaAdmin(): KafkaAdmin {
        val configs = HashMap<String, Any>()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        return KafkaAdmin(configs)
    }

    @Bean
    open fun mySpringTopic(): NewTopic {
        return NewTopic(TOPIC, 1, 1)
    }

    @Bean
    open fun consumerFactory(): ConsumerFactory<String, String> {
        println("creating consumer factory with groupId=$groupId @bootstrapAddress $bootstrapAddress")
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        configProps[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        configProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        configProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return DefaultKafkaConsumerFactory(configProps)
    }

    @Bean
    open fun kafkaListenerContainerFactory(consumerFactory: ConsumerFactory<String, String>): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory
        return factory
    }

    @PostConstruct
    fun constructionComplete() = println("This app is a consumer! groupId: $groupId")
}