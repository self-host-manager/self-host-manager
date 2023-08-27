package org.ivcode.shm.params.service

import org.ivcode.shm.params.repository.ParamGroupDao
import org.ivcode.shm.params.repository.ParamValueDao
import org.ivcode.shm.params.repository.entities.ParamGroupEntity
import org.ivcode.shm.params.repository.entities.ParamValueEntity
import org.ivcode.shm.params.repository.entities.ParamValueEntityKey
import org.ivcode.shm.params.service.domain.ParamValue
import org.ivcode.shm.params.service.domain.SaveParam
import org.ivcode.shm.params.service.domain.toDomain
import org.ivcode.shm.utils.createRandomPassword
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ParameterService (
    private val paramGroupDao: ParamGroupDao,
    private val paramValueDao: ParamValueDao,
) {
    fun listGroups(): List<String> =
        paramGroupDao.findAll().map { grp -> grp.name!! }

    fun deleteGroup(group: String) =
        paramGroupDao.deleteById(group)

    @Transactional
    fun createGroup(group: String): ParamGroupEntity =
        paramGroupDao.saveAndFlush(ParamGroupEntity(name=group))

    fun listParams(group: String): List<ParamValue> =
        paramValueDao.findByGroup(group).map { value -> value.toDomain() }

    @Transactional
    fun saveParams(
        group: String,
        name: String,
        saveParam: SaveParam
    ) {
        // make sure the group exists
        val value = createValue(saveParam)
        paramValueDao.saveAndFlush(ParamValueEntity(
            key = ParamValueEntityKey(
                paramGroupName = group,
                name = name
            ),
            value = value
        ))
    }

    fun createValue (
        saveParam: SaveParam
    ): String {
        if(saveParam.value==null && saveParam.random==null) {
            throw IllegalArgumentException("value must be set or set to random")
        }
        if(saveParam.value!=null && saveParam.random!=null) {
            throw IllegalArgumentException("value must be set or set to random, but not both")
        }

        return if(saveParam.random!=null) {
            val random = saveParam.random

            createRandomPassword(
                length = random.length,
                enableUpper = random.enableUpper,
                minimumUpper = random.minimumUpper,
                enableLower = random.enableLower,
                minimumLower = random.minimumLower,
                enableNumber = random.enableNumber,
                minimumNumber = random.minimumNumber,
                enableSpecial = random.enableSpecial,
                minimumSpecial = random.minimumSpecial
            )
        } else {
            saveParam.value!!
        }
    }
}