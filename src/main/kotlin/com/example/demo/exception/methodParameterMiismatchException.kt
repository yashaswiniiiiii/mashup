package com.example.demo.exception

class MethodArgumentNotValidExceptionn(val errorCode: Int, message: String): RuntimeException(message)