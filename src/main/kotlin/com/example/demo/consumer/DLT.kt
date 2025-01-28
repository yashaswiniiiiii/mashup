package com.example.demo.consumer

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.pulsar.annotation.PulsarListener
import org.springframework.stereotype.Service

@Service
class DeadLetterTopicConsumer {

    private val log: Logger = LoggerFactory.getLogger(DeadLetterTopicConsumer::class.java)

    @PulsarListener(
        topics = ["dlt-topic"],
        subscriptionName = "dlt-subscription"
    )
    fun consumeDLTMessage(msg: String) {
        log.error("Received message in DLT: $msg")
        // Add your DLT handling logic (e.g., save to DB, send alert, etc.)
    }
}
