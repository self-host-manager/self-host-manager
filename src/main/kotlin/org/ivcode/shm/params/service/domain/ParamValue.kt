package org.ivcode.shm.params.service.domain

import org.ivcode.shm.params.repository.entities.ParamValueEntity

data class ParamValue(
    val group: String,
    val name: String,
    val value: String
)

fun ParamValueEntity.toDomain(): ParamValue = ParamValue(
    group = this.key!!.paramGroupName!!,
    name = this.key!!.name!!,
    value = this.value!!
)