package com.github.stephenott.qtz.zeebe

import io.kotlintest.specs.AnnotationSpec
import io.kotlintest.specs.StringSpec
import io.micronaut.test.support.TestPropertyProvider
import io.zeebe.client.ZeebeClient

abstract class ZeebeSpecification(): StringSpec(), TestPropertyProvider {

    private val instance by lazy { ZeebeContainer().apply { start() } }

    open fun getZeebeClient(): ZeebeClient {
        return ZeebeClient.newClientBuilder()
                .brokerContactPoint(String().plus(instance.getClientPort()))
                .build()
    }

    open fun getBroker(): String {
        return instance.getBroker()
    }

    override fun getProperties(): MutableMap<String, String> {
        return mutableMapOf("orchestrator.management.client.brokerContactPoint" to getBroker())
    }
}