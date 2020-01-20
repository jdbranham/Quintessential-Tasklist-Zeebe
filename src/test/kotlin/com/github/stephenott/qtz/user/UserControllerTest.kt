package com.github.stephenott.qtz.user

import io.kotlintest.specs.StringSpec
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest


@MicronautTest
class UserControllerTest(@Client("/") var client: HttpClient) : StringSpec({

    "should create ROLE_USER"{
        val requestBody = mapOf(
                "username" to "testUser",
                "password" to "testPassword",
                "roles" to listOf("ROLE_USER")
        )
        val response = client.exchange(HttpRequest.POST("/roles", requestBody), Map::class.java)
        println(response)
    }

    "should create a user"{
        val requestBody = mapOf(
            "username" to "testUser",
            "password" to "testPassword",
            "roles" to listOf("ROLE_USER")
        )

        val request = HttpRequest.POST("/users", requestBody)
                .contentType(MediaType.APPLICATION_JSON_TYPE)
        val response = client.exchange(request, Map::class.java)
        println(response)
    }
})