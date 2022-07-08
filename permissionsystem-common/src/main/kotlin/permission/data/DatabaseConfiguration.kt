package permission.data

internal data class DatabaseConfiguration(
    val databaseType: String,
    val databaseName: String,
    val host: String,
    val port: Int,
    val user: String,
    val password: String
)