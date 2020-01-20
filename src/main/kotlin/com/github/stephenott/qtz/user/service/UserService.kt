package com.github.stephenott.qtz.user.service

import com.github.stephenott.qtz.user.controller.CreateUserRequest
import com.github.stephenott.qtz.user.domain.RoleEntity
import com.github.stephenott.qtz.user.domain.UserEntity
import com.github.stephenott.qtz.user.repository.RoleRepository
import com.github.stephenott.qtz.user.repository.UserRepository
import io.micronaut.security.authentication.providers.PasswordEncoder
import io.reactivex.Single
import javax.inject.Singleton
import javax.management.relation.RoleNotFoundException
import javax.transaction.Transactional

@Singleton
open class UserService(
        private val passwordEncoder: PasswordEncoder,
        private val userRepository: UserRepository,
        private val roleRepository: RoleRepository
): UserServiceOperations {

    @Transactional
    override fun addUser(request: CreateUserRequest): Single<UserEntity> {

        val user = userRepository.findById(request.username).blockingGet()
        return if(user == null){
            userRepository.save(
                    UserEntity(
                            _password = passwordEncoder.encode(request.password),
                            accountExpired = false,
                            accountLocked = false,
                            enabled = true,
                            id = request.username,
                            passwordExpired = false,
                            roles = request.roles.map{ getRole(it) }.toSet()
                    )
            )
        } else {
            Single.error(UserAlreadyExistsException(request.username))
        }
    }

    override fun findByUsername(username: String): Single<UserEntity> {
        return userRepository.findById(username).flatMapSingle { Single.just(it) }.onErrorResumeNext(Single.error(UserNotFoundException(username)))
    }

    override fun ensureRoleExists(authority: String): Single<RoleEntity> {
        return roleRepository.findById(authority).switchIfEmpty(
            Single.just(RoleEntity(authority = authority, users = setOf())).flatMap {
                roleRepository.save(it)
            }
        ).doAfterSuccess {
            Single.just(it)
        }
    }

    private fun getRole(authority: String): RoleEntity {
        return roleRepository.findById(authority).switchIfEmpty(Single.error(RoleNotFoundException("$authority not found"))).blockingGet()
    }
}

class UserAlreadyExistsException(username: String): RuntimeException("username: $username already exists")
class UserNotFoundException(username: String?): RuntimeException("username: $username does not exist")

interface UserServiceOperations {
    fun addUser(request: CreateUserRequest): Single<UserEntity>
    fun findByUsername(username: String): Single<UserEntity>
    fun ensureRoleExists(authority: String): Single<RoleEntity>
}