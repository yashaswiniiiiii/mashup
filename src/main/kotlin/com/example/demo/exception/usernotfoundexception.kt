package com.example.demo.exception

class UserNotFoundExceptionn(val errorCode: Int, message: String): RuntimeException(message)