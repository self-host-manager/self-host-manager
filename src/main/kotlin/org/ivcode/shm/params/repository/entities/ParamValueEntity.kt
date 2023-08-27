package org.ivcode.shm.params.repository.entities

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "param_value")
data class ParamValueEntity (

    @EmbeddedId
    val key: ParamValueEntityKey? = null,

    @Column(name="val")
    val value: String? = null,
)

@Embeddable
data class ParamValueEntityKey (
    val name: String? = null,

    @Column(name="param_group_name")
    val paramGroupName: String? = null,
): Serializable