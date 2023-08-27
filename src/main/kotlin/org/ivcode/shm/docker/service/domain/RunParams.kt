package org.ivcode.shm.docker.service.domain

data class RunParams(
    /** image name */
    val image: String,

    /** container name */
    val name: String,

    /** The calling service. This is added as a container label */
    val service: String,

    /** environmental variables */
    val env: List<String>?
)
