package com.tired.parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

object JacksonParserFactory {

    fun create() = ObjectMapper().registerModule(JavaTimeModule()).registerModule(KotlinModule())

}