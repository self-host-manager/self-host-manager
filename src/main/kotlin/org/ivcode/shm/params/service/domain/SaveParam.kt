package org.ivcode.shm.params.service.domain

data class SaveParam (
    val value: String?,
    val random: RandomPassword?
)
data class RandomPassword (
    val length: Int = 16,
    val enableUpper: Boolean = true,
    val minimumUpper: Int? = null,
    val enableLower: Boolean = true,
    val minimumLower: Int? = null,
    val enableNumber: Boolean = true,
    val minimumNumber: Int? = null,
    val enableSpecial: Boolean = true,
    val minimumSpecial: Int? = null
)