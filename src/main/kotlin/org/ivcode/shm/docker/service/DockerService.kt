package org.ivcode.shm.docker.service

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.command.AsyncDockerCmd
import org.ivcode.shm.docker.service.domain.RunParams
import org.ivcode.shm.docker.service.exception.ContainerExistsException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.Closeable
import kotlin.coroutines.suspendCoroutine

/**
 * Higher level operations for docker
 */
@Service
class DockerService (
    val dockerClient: DockerClient
) {

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(DockerService.javaClass)
        private val CONTAINER_LABELS = mapOf(
            "application" to "self-host-manager"
        )
    }

    suspend fun run(params: RunParams) {
        LOGGER.debug("RUN: START")

        if(this.hasContainer(params.name)) {
            throw ContainerExistsException("container exists: ${params.name}")
        }

        if(!this.hasImage(params.image)) {
            dockerClient.pullImageCmd(params.image).exec()
        }

        dockerClient.createContainerCmd(params.image).apply {
            withName(params.name)

            val labels = CONTAINER_LABELS.toMap(mutableMapOf("service" to params.service))
            withLabels(labels)

            if(params.env!=null) {
                withEnv(params.env)
            }
        }.exec()

        startContainer(params.name)
        LOGGER.debug("RUN: STOP")
    }

    fun startContainer(name: String) {
        dockerClient.startContainerCmd(name).exec()
    }

    fun hasContainer(name: String, filtered: Boolean = false): Boolean {
        val name = "/${name}"
        val containers = dockerClient.listContainersCmd().apply {
            withShowAll(true)
            if(filtered) {
                withLabelFilter(CONTAINER_LABELS)
            }
        }.exec()

        for(container in containers) {
            if(container.names.contains(name)) {
                return true
            }
        }

        return false
    }

    fun hasImage(image: String):Boolean {
        val images = dockerClient.listImagesCmd().exec()

        for(img in images) {
            for(tag in img.repoTags) {
                if(tag.equals(image)) {
                    return true
                }
            }
        }

        return false
    }
}

/**
 * Converts the async exec operation into a coroutine
 */
suspend fun <CMD_T : AsyncDockerCmd<CMD_T, A_RES_T>, A_RES_T> AsyncDockerCmd<CMD_T, A_RES_T>.exec(): List<A_RES_T> = suspendCoroutine {
    this.exec(object : ResultCallback<A_RES_T> {
        var results: MutableList<A_RES_T> = mutableListOf()
        var error: Throwable? = null

        override fun close() {
        }

        override fun onStart(closeable: Closeable) {
        }

        override fun onError(throwable: Throwable) {
            error = throwable
        }

        override fun onComplete() {
            if(error != null) {
                it.resumeWith(Result.failure(error!!))
            } else {
                it.resumeWith(Result.success(results))
            }
        }

        override fun onNext(responseItem: A_RES_T) {
            this.results.add(responseItem)
        }
    })
}