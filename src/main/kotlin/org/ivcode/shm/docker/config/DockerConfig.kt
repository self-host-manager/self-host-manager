package org.ivcode.shm.docker.config

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.DockerClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class DockerConfig {

    @Bean
    fun createDockerClient(): DockerClient {
        return DockerClientBuilder.getInstance().build()
    }

}