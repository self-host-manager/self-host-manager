package org.ivcode.shm

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class Main

fun main(args: Array<String>) {
    SpringApplication.run(Main::class.java, *args)
}