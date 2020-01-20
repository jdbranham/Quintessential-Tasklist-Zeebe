package com.github.stephenott.qtz.authentication

import com.github.stephenott.qtz.user.controller.CreateUserRequest
import com.github.stephenott.qtz.user.domain.UserEntity
import com.github.stephenott.qtz.user.service.UserServiceOperations
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import io.micronaut.security.authentication.providers.AuthoritiesFetcher
import io.micronaut.security.authentication.providers.PasswordEncoder
import io.micronaut.security.authentication.providers.UserFetcher
import io.micronaut.security.authentication.providers.UserState
import io.reactivex.Flowable
import io.reactivex.Single
import org.reactivestreams.Publisher
import java.security.MessageDigest
import javax.inject.Singleton
import kotlin.streams.toList


@Singleton
open class NativeUserAuthorization(
        private val userService: UserServiceOperations,
        private val securityConfiguration: SecurityConfiguration): ApplicationEventListener<ServerStartupEvent> {

    override fun onApplicationEvent(event: ServerStartupEvent?) {
        if(securityConfiguration.createDefaultAdmin) {
            userService.ensureRoleExists(securityConfiguration.adminRole)
                    .doOnSuccess {
                        userService.findByUsername(securityConfiguration.defaultAdminUsername)
                                .flatMap {
                                    Single.just(it)
                                }.onErrorResumeNext {
                                    userService.addUser(CreateUserRequest(
                                            roles = setOf<String>(securityConfiguration.adminRole),
                                            password = securityConfiguration.defaultAdminPassword,
                                            username = securityConfiguration.defaultAdminUsername))
                                }.doFinally {
                                    println("complete")
                                }.subscribe()
                    }.subscribe()
        }
    }
}

@Singleton
class UserFetcherService(private val userService: UserServiceOperations): UserFetcher {
    override fun findByUsername(username: String?): Publisher<UserState> {
        return if(username == null) {
            Flowable.empty()
        } else {
            userService.findByUsername(username)
                    .onErrorReturn { getAnonymousUser() }
                    .flatMapPublisher { Flowable.just(it) }
        }
    }
}

@Singleton
open class AuthoritiesFetcherService(
        private val userService: UserServiceOperations,
        private val securityConfiguration: SecurityConfiguration): AuthoritiesFetcher {
    override fun findAuthoritiesByUsername(username: String?): Publisher<MutableList<String>> {
        return if (username == null) {
            Flowable.just(MutableList(0) {securityConfiguration.anonymousRole})
        } else {
            userService.findByUsername(username).flatMapPublisher { u ->
                Flowable.just( u.roles.stream().map { it.authority }.toList().toMutableList() )
            }
        }
    }
}

@Singleton
class DefaultPasswordEncoder : PasswordEncoder {
    private val md = MessageDigest.getInstance("SHA-256")

    override fun matches(rawPassword: String?, encodedPassword: String?): Boolean {
        return encodedPassword == encode(rawPassword)
    }

    override fun encode(rawPassword: String?): String {
        return String(md.digest((rawPassword?:"").toByteArray()))
    }
}


private fun getAnonymousUser(): UserEntity? {
    return UserEntity(
        enabled = true,
        roles = emptySet(),
        passwordExpired = false,
        id = "anonymous",
        accountLocked = false,
        accountExpired = false,
        _password = ""
    )
}