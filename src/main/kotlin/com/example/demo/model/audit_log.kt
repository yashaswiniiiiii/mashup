package com.example.demo.model


import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity
@Table(name = "audit_log")

data class AuditLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?=null,
    @Column(nullable = false)
    val messageContent: String,

    @Column(nullable = false)
    val messageId: String,

    @Column(nullable = false)
    val processedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = true)
    var status: String? = null,

    @Column(nullable = true)
    var errorMessage: String? = null,

    @Column(nullable = true)
    val processedBy: String? = null

)