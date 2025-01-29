package com.example.demo.producer

import org.apache.pulsar.client.api.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class Publisher(
    @Value("\${spring.pulsar.client.service-url}") private val serviceUrl: String
) {
    private lateinit var pulsarClient: PulsarClient
    private lateinit var producer: Producer<String>


    init {
        try {
            pulsarClient = PulsarClient.builder()
                .serviceUrl(serviceUrl)
                .build()

            producer = pulsarClient.newProducer(Schema.STRING)
                .topic("topic-name")
                .create()
        } catch (e: PulsarClientException) {
            throw RuntimeException("Failed to initialize Pulsar client: ${e.message}", e)
        }
    }


    fun publishMessage(messageContent: String) {
        val messageFuture: CompletableFuture<MessageId> = producer.sendAsync(messageContent)

        messageFuture.whenComplete { messageId: MessageId?, throwable: Throwable? ->
            if (throwable != null) {
                println("Failed to send message: $throwable")
            } else {
                println("Message sent successfully with ID: $messageId")
            }
        }
    }
}
