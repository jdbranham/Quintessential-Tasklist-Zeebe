package com.github.stephenott.qtz.authentication

import io.kotlintest.specs.StringSpec
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import javax.inject.Inject

@MicronautTest
class SecurityConfigurationTest: StringSpec(), TestPropertyProvider {

    @Inject lateinit var securityConfiguration: SecurityConfiguration

    init {
        "should load the security config from the properties"{
            println("${securityConfiguration.rolePermissions}")
            assert(securityConfiguration.rolePermissions.isNotEmpty()) {"it should have at least one"}
        }
    }

    override fun getProperties(): MutableMap<String, String> {
        return mutableMapOf(
            "security.role-permissions[0].role" to "ROLE_ADMIN",
            "security.role-permissions[0].permissions[0]" to "TASK_GET",
            "security.create-default-admin" to "false"
        )
    }
}