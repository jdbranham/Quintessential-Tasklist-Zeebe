package com.github.stephenott.qtz.ui

import io.micronaut.context.annotation.Property
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.security.utils.SecurityService
import io.micronaut.views.View
import java.security.Principal
import javax.inject.Inject

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller
class DefaultController(private val securityService: SecurityService?) {

    @set:Inject
    @setparam:Property(name = "micronaut.security.oauth2.enabled")
    var oauthEnabled: Boolean = false

    @View("index")
    @Get
    fun index(principal: Principal?): Map<String?, Any?>? {
        securityService?.authentication?.ifPresent {t: Authentication ->
            println("${t.name}")
            println("${t.attributes}")
        }
        return mapOf(
            "oauthEnabled" to oauthEnabled,
            "loggedIn" to (principal != null),
            "username" to principal?.name
        )
    }

    @View("login")
    @Get("/login/auth")
    fun login(): Map<String?, Any?>? {
        return HashMap()
    }

    @View("login")
    @Get("/login/authFailed")
    fun loginFailed(): Map<String?, Any?>? {
        return mapOf(
            "errors" to true
        )
    }
}