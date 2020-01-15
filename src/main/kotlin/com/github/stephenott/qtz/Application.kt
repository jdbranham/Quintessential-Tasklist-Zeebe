package com.github.stephenott.qtz

import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
        info = Info(
                title = "QTZ API",
                version = "0.0.1",
                description = "Quintessential Task List for Zeebe")
)
object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.github.stephenott.qtz")
                .mainClass(Application.javaClass)
                .start()
    }
}