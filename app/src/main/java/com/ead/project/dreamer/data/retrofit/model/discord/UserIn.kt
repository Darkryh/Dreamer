package com.ead.project.dreamer.data.retrofit.model.discord

data class UserIn(
    val avatar: String?,
    val discriminator: String,
    val id: String,
    val public_flags: Int,
    val username: String
)