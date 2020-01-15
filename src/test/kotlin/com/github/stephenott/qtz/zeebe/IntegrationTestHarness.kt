package com.github.stephenott.qtz.zeebe

import io.zeebe.client.ZeebeClient

object IntegrationTestHarness {

    val instance by lazy { ZeebeContainer().apply { start() } }

    fun getZeebeClient(): ZeebeClient {
        return ZeebeClient.newClientBuilder()
                .brokerContactPoint(String().plus(instance.getClientPort()))
                .build()
    }
}