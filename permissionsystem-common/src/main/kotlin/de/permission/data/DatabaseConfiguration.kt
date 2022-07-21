package de.permission.data

import kotlinx.serialization.Serializable

@Serializable
internal data class DatabaseConfiguration(
    val databaseType: String = "mongodb",
    val databaseName: String = "permissionPlayer",
    val host: String = "127.0.0.1",
    val port: Int = 27017,
    val user: String = "username",
    val password: String = "password"
)