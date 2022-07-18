package permission.data

import kotlinx.serialization.Serializable
import permission.serialization.DefaultJsonConfiguration

@Serializable
internal data class DatabaseConfiguration(
    val databaseType: String,
    val databaseName: String,
    val host: String,
    val port: Int,
    val user: String,
    val password: String
) : DefaultJsonConfiguration