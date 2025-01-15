package com.example.demo.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "\"User\"")

data class User(
    @Id
    @field:NotNull("User id can't be null")
    val id: Long?,
    @field:NotNull("User name can't be null")
    val name: String?,
    @field:NotNull("User age can't be null")
    val age: Int?,
    @field:NotNull("User address can't be null")
    val address: String?,
    @field:NotNull("User PhoneNumber can't be null")
    val phoneNumber: String?
)