package com.github.stephenott.qtz.zeebe

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

class ZeebeContainer: GenericContainer<ZeebeContainer>("camunda/zeebe"){
    private val zeebePort = 26500
    init {
        withExposedPorts(zeebePort)
        waitingFor(Wait.forListeningPort())
    }
    fun getClientPort(): Int {
        return this.getMappedPort(zeebePort)
    }
    fun getBroker(): String {
        return "localhost:${getClientPort()}"
    }
}