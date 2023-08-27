package org.ivcode.shm.docker.service.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ContainerExistsException (
    message: String,
    cause: Throwable? = null
): RuntimeException (message, cause)