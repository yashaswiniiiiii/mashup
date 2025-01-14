package com.example.demo


import com.example.demo.exception.MethodArgumentNotValidExceptionn
import com.example.demo.exception.UserNotFoundExceptionn
import com.example.demo.model.ErrorResponse
import com.example.demo.exception.BadRequestException
import com.example.demo.exception.conflictException
import com.example.demo.model.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import kotlin.collections.any
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.support.SqlValue
import org.springframework.validation.BindingResult

@RestController
@RequestMapping("/users")
class UserController @Autowired constructor(val userRepository: UserRepository) {
   private val logger: Logger= LoggerFactory.getLogger(UserController:: class.java)

    @PostMapping
    fun createUser(@Valid @RequestBody newUser: User): ResponseEntity<User> {
     logger.info("Creating new User with Id ${newUser.id}")
        val validationErrors=mutableListOf<String>()

        if(newUser.id == null){
            validationErrors.add("User ID must be not Null")
        }
        if(newUser.name.isNullOrBlank()){
            validationErrors.add("User name must be not Null")
        }
        if(newUser.address.isNullOrBlank()){
            validationErrors.add("User address must be not Null")
        }

        newUser.id?.let {
            if (newUser.id !is Long || it <= 0) {
                throw BadRequestException(400,"User ID must be greater than zero.")
            }
        }
        newUser.age?.let {
            if (it <= 0) {
                throw BadRequestException(400,"User Age must be greater than zero.")
            }
        }

//       val userId: Long = try {
//            newUser.id?.toLong() ?:
//        } catch (e: NumberFormatException) {
//            throw BadRequestException(400,"User ID '${newUser.id}' is not a valid number. Please provide a valid userId.")
//        }
        val existingUser = userRepository.findById(newUser.id ?: -1).orElse(null)
        if (existingUser != null) {
            throw conflictException(409, "A user with ID ${newUser.id} already exists.")
        }
//        userRepository.findById(newUser.id ?: -1).ifPresent {
//            throw conflictException(409, "Already a user with ID ${newUser.id} exists")
//        }
        val savedUser = userRepository.save(newUser)
        logger.info("new user with id {newUser.id} has been created")
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser)
    }



    @GetMapping

    fun listUsers(): List<User> {
        val users:List<User>
        users=userRepository.findAll()
        logger.info("Fetching details")
        return users
    }






    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<User> {
        logger.info("getting details of user having id${id}")
        val user = userRepository.findById(id).orElseThrow {

            UserNotFoundExceptionn(404, "User with id $id not found")
        }
        logger.info("details of user having id${id} is fetched")
        return ResponseEntity.ok(user)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        logger.info("Deleting user having id${id}")
        return userRepository.findById(id).map { user ->
            userRepository.delete(user)
            logger.info("User with Id ${id} deleted successfully")
            ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }.orElseThrow {
            // Passing error code along with the message
            UserNotFoundExceptionn(404, "User with id $id not found")
        }
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody updatedUser: User
    ): ResponseEntity<User> {
        logger.info("Updating details of user having id${id}")
        val existingUser = userRepository.findById(id).orElseThrow {
            // Passing error code along with the message
            UserNotFoundExceptionn(404, "User with id $id not found")
        }

        val newUser = existingUser.copy(
            name = updatedUser.name,
            age = updatedUser.age,
            address = updatedUser.address,
            phoneNumber = updatedUser.phoneNumber
        )
        logger.info("details of user having id${id} is updated")
        return ResponseEntity.ok(userRepository.save(newUser))
    }
    @GetMapping("/{name}/search")
    fun searchuser(@PathVariable name: String): ResponseEntity<List<User>>{
    val users : List<User>
       users= userRepository.findByNameContainingIgnoreCase(name)
        return if(users.isNotEmpty()){
            ResponseEntity.ok(users)
        }
        else{
            ResponseEntity.noContent().build()
        }
    }
}
