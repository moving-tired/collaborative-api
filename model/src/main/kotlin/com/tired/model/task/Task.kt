package com.tired.model.task

import java.time.LocalDateTime

abstract class Task(
    open val id: String,
    open val description: String,
    open val name: String,
    open val category: String,
    open val owner: String,
    open val location: Location,
    open val createdAt: LocalDateTime
)