package com.github.stephenott.qtz.user.repository

import com.github.stephenott.qtz.user.domain.RoleEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.reactive.RxJavaCrudRepository

@Repository
interface RoleRepository: RxJavaCrudRepository<RoleEntity, String>