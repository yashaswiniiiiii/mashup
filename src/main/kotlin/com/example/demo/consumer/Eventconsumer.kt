package com.example.demo.consumer

import com.example.demo.AuditLogRepo
import com.example.demo.model.AuditLog
import org.apache.pulsar.client.api.Message
import org.springframework.pulsar.annotation.PulsarListener
import org.springframework.retry.annotation.Retryable
import org.springframework.retry.annotation.Backoff
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

@Service
class Eventconsumer @Autowired constructor(private val auditLogRepository: AuditLogRepo){

    private val logger = LoggerFactory.getLogger(Eventconsumer::class.java)


    @PulsarListener(
        topics = ["topic-name"],
        subscriptionName = "my-subscription",
    )
    @Retryable(
        maxAttempts = 3,
        backoff = Backoff(delay = 2000)
    )
    fun consumeMessage(message: Message<String>) {
        val content = message.value
        logger.info("Consumed message: $content")
        val auditLog = AuditLog(
            messageContent = content,
            messageId = message.messageId.toString(),
            status = "Success",
            processedAt = LocalDateTime.now()
        )
  try {
      if (content.contains("Error")) {
          logger.error("Error in Processing Message: $content")
          throw RuntimeException("Simulated exception for redelivery testing")
      }
      auditLog.status = "Success"
      logger.info("Message processed successfully: $content")

  } catch (e: Exception) {

      auditLog.status = "Failure"
      auditLog.errorMessage = e.message
      logger.error("Failed to process message: ${e.message}")
  } finally {

      auditLogRepository.save(auditLog)
  }



    }
}
