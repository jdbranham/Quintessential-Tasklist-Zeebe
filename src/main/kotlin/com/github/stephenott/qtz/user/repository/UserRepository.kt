package com.github.stephenott.qtz.user.repository

import com.github.stephenott.qtz.user.domain.UserEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.reactive.RxJavaCrudRepository

@Repository
interface UserRepository: RxJavaCrudRepository<UserEntity, String>