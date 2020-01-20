package com.github.stephenott.qtz.user.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class RoleEntity(
        @field:Id val authority: String,

        @field:OneToMany(fetch = FetchType.EAGER)
        @field:JsonIgnore val users: Set<UserEntity>
)