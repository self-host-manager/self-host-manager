package org.ivcode.shm.params.controller

import org.ivcode.shm.params.service.ParameterService
import org.ivcode.shm.params.service.domain.ParamValue
import org.ivcode.shm.params.service.domain.SaveParam
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/params")
class ParameterController(
    private val parameterService: ParameterService
) {

    @GetMapping
    fun listGroups(): List<String> = parameterService.listGroups()

    @PostMapping(path = ["/{group}"])
    fun createGroup(@PathVariable group: String) =
        parameterService.createGroup(group)

    @DeleteMapping(path = ["/{group}"])
    fun deleteGroup(@PathVariable group: String) =
        parameterService.deleteGroup(group)

    @GetMapping(path = ["/{group}"])
    fun listNames(
        @PathVariable group: String
    ): List<ParamValue> = parameterService.listParams(
        group = group
    )

    @PostMapping(path = ["/{group}/{name}"])
    fun post(
        @PathVariable group: String,
        @PathVariable name: String,
        @RequestBody saveParam: SaveParam
    ) = parameterService.saveParams(
        group = group,
        name = name,
        saveParam = saveParam
    )
}
