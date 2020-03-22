package com.tired.rest.user

import com.tired.rest.AbstractRestTest
import org.junit.jupiter.api.Test

class GetUserRestTest: AbstractRestTest() {

    @Test
    fun getUser() {
        webTestClient.get().uri("/api/v1/users/1")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody()
            .consumeWith { print(it) }
    }

}