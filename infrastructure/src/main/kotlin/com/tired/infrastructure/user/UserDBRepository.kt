package com.tired.infrastructure.user

import com.tired.model.user.User
import com.tired.model.user.UserRepository
import com.tired.model.user.exceptions.UserNotFoundException
import com.zaxxer.hikari.HikariDataSource
import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using

class UserDBRepository(private val hikariDataSource: HikariDataSource) : UserRepository {

    companion object {
        private const val GET_USERS = "SELECT * FROM users WHERE id = :id"
    }

    override fun get(id: Long): User {
        return using(sessionOf(hikariDataSource)) {
            it.run(
                queryOf(GET_USERS, mapOf("id" to id)).map {
                    User(
                        id = it.long("id"),
                        name = it.string("name")
                    )
                }.asSingle
            )
        } ?: throw UserNotFoundException()
    }
}