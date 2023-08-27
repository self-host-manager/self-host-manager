package org.ivcode.shm.params.repository.entities

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "param_group")
data class ParamGroupEntity (
    @Id val name: String? = null
)
