package com.tired.model.task

import com.tired.model.user.User
import java.time.LocalDateTime

data class VoluntaryTask(
    override val id: String,
    override val name: String,
    override val description: String,
    override val category: String,
    override val owner: User,
    override val location: Location,
    override val createdAt: LocalDateTime,
    val voluntary: User?,
    val updatedAt: LocalDateTime
) : Task(
    id, name, description, category, owner, location, createdAt
)