package com.tired.model.user

import java.time.LocalDateTime

data class User(val id: Long, val name: String, val createdDate: LocalDateTime = LocalDateTime.now())