package com.github.stephenott.qtz.user.controller

import com.github.stephenott.qtz.user.domain.RoleEntity
import com.github.stephenott.qtz.user.domain.UserEntity
import com.github.stephenott.qtz.user.service.UserService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.reactivex.Single


@Controller("/users")
open class UserController(private val userService: UserService): UserControllerOperations {

    @Post
    override fun addUser(@Body request: Single<CreateUserRequest>): Single<HttpResponse<UserEntity>> {
        return request.flatMap {
            userService.addUser(it)
        }
        .flatMap {
            Single.just(HttpResponse.created(it))
        }
    }

    @Post("/role")
    override fun addRole(@Body request: Single<CreateRoleRequest>): Single<HttpResponse<RoleEntity>> {
        return request.flatMap {
            userService.ensureRoleExists(it.authority).flatMap { r ->
                Single.just(HttpResponse.created(r))
            }
        }
    }
}

interface UserControllerOperations {
    fun addUser(request: Single<CreateUserRequest>): Single<HttpResponse<UserEntity>>
    fun addRole(request: Single<CreateRoleRequest>): Single<HttpResponse<RoleEntity>>
}