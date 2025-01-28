package com.example.demo
import java.time.LocalDateTime
data class Audit(
    val eventId: String,
    val eventType: String,
    val userId: String,
    val timestamp: LocalDateTime,
    val details: String
)