package org.tired

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = ["org.tired"])

class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
