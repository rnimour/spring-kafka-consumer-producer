/*
 * This source file was generated by the Gradle 'init' task
 */
package com.rnimour.messaging.kafka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Producer {
    val greeting: String
        get() {
            return "Hello World! I produce Kafka messages!"
        }
}

fun main(args: Array<String>) {
    println(Producer().greeting)
    runApplication<Producer>(*args)
}
