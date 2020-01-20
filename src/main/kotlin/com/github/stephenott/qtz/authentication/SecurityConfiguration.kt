package com.github.stephenott.qtz.authentication

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Context

@Context
@ConfigurationProperties("security")
class SecurityConfiguration {
    var anonymousRole = "ROLE_ANONYMOUS"
    var adminRole = "ROLE_ADMIN"
    var userRole = "ROLE_USER"

    var defaultAdminUsername: String = "admin"
    var defaultAdminPassword: String = "admin"
    var createDefaultAdmin = true

    var rolePermissions: List<RolePermissions> = listOf()

    @ConfigurationProperties("role-permissions")
    class RolePermissions {
        val role: String = ""
        val permissions: List<AppPermission> = listOf()
    }
}


enum class AppPermission {
    FORM_GET,
    TASK_GET,
    TASK_CREATE,
    TASK_ASSIGN,
    TASK_CLAIM,
    TASK_UNCLAIM,
    TASK_COMPLETE,
    TASK_SUBMIT,
    USER_CREATE,
    USER_ROLE_CREATE
}