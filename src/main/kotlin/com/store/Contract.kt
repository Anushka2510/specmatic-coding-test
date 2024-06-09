package com.store

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Contract

fun main(args: Array<String>) {
    runApplication<Contract>(*args)
}