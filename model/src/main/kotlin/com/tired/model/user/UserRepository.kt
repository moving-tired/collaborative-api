package com.tired.model.user

interface UserRepository {

    fun get(id: Long): User

}