package com.github.stephenott.qtz.user.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import io.micronaut.security.authentication.providers.UserState
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class UserEntity(@field:Id
                val id: String,
                val _password: String,
                var enabled: Boolean = true,
                var accountExpired: Boolean = false,
                var accountLocked: Boolean = false,
                var passwordExpired: Boolean = false,
                @field:OneToMany(fetch = FetchType.EAGER)
                var roles: Set<RoleEntity>) : UserState {

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun isPasswordExpired(): Boolean {
        return passwordExpired
    }

    override fun getUsername(): String {
        return id
    }

    override fun isAccountExpired(): Boolean {
        return accountExpired
    }
    @JsonIgnore
    override fun getPassword(): String {
        return _password
    }

    override fun isAccountLocked(): Boolean {
        return accountLocked
    }
}