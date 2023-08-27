package org.ivcode.shm.params.repository

import org.ivcode.shm.params.repository.entities.ParamValueEntity
import org.ivcode.shm.params.repository.entities.ParamValueEntityKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ParamValueDao: JpaRepository<ParamValueEntity, ParamValueEntityKey> {

    @Query("SELECT v FROM ParamValueEntity v WHERE param_group_name = :group")
    fun findByGroup(group: String): List<ParamValueEntity>

    @Query("DELETE ParamValueEntity WHERE param_group_name = :group")
    fun deleteByGroup(group: String)
}