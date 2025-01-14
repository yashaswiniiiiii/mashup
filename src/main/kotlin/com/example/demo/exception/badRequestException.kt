package com.example.demo.exception

class BadRequestException(val errorCode: Int,message: String) : RuntimeException(message)
