package com.example.demo

import com.example.demo.model.AuditLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuditLogRepo : JpaRepository<AuditLog, Long>