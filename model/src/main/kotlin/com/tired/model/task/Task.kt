package com.tired.model.task

import com.tired.model.user.User
import java.time.LocalDateTime

abstract class Task(
    open val id: String,
    open val description: String,
    open val name: String,
    open val category: String,
    open val owner: User,
    open val location: Location,
    open val createdAt: LocalDateTime
)