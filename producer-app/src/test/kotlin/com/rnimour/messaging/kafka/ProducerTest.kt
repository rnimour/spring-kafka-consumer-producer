/*
 * This source file was generated by the Gradle 'init' task
 */
package com.rnimour.messaging.kafka

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test


@SpringBootTest
class ProducerTest {

    @Value(value = "test.property")
    var testProperty = "default"

    @Test
    fun contextLoads() {
    }

    @Test
    fun testResolveProperty() {
        // see application-test.yml, which overrides test.property
        assertThat(testProperty).isNotEqualTo("default")
    }
}
