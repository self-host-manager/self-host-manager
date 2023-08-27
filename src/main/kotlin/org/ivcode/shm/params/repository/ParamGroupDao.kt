package org.ivcode.shm.params.repository

import org.ivcode.shm.params.repository.entities.ParamGroupEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParamGroupDao: JpaRepository<ParamGroupEntity, String>