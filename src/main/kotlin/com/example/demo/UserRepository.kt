package com.example.demo
import com.example.demo.model.User
import org.springframework.data.jpa.repository.JpaRepository
interface UserRepository : JpaRepository<User, Long>{
    fun findByNameContainingIgnoreCase(string: String): List<User>
}