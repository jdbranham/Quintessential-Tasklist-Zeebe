package com.github.stephenott.qtz.zeebe

import io.kotlintest.shouldNotBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.multipart.MultipartBody
import io.micronaut.test.annotation.MicronautTest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import javax.inject.Inject

@MicronautTest
class ZeebeBroker1Test() : ZeebeSpecification() {

    @Inject @field:Client("/") lateinit var client: RxHttpClient

    private val log: Logger = LoggerFactory.getLogger(ZeebeBroker1Test::class.java)

    init {
        "the zeebe client should be available" {
            val zeebeClient = getZeebeClient()
            println("CLIENT is RUNNING -->>${zeebeClient.configuration}")
            zeebeClient shouldNotBe null
        }

        "deploy a workflow" {
            val file = File("src/test/resources/simpleUserTask.bpmn")

            val requestBody = MultipartBody.builder()
                    .addPart("workflow",
                            file.name,
                            MediaType.TEXT_PLAIN_TYPE,
                            file).build()

            val dog = HttpRequest.POST("/zeebe/management/workflow/deployment", requestBody)
                    .contentType(MediaType.MULTIPART_FORM_DATA_TYPE)
            val response = client.toBlocking().exchange(dog, Map::class.java)
            log.info("$response")
        }
    }
}