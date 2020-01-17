package com.github.stephenott.qtz.ui

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View
import java.util.HashMap


@Controller
class DefaultController {

    @Secured(SecurityRule.IS_ANONYMOUS)
    @View("index")
    @Get
    fun index(): Map<String?, Any?>? {
        return HashMap()
    }
}