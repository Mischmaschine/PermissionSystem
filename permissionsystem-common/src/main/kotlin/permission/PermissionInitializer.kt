package permission

import de.mischmaschine.database.database.Configuration
import de.mischmaschine.database.mongodb.AbstractMongoDB
import de.mischmaschine.database.sql.local.h2.AbstractH2SQL
import de.mischmaschine.database.sql.local.sqlite.AbstractSQLite
import de.mischmaschine.database.sql.network.mariadb.AbstractMariaDB
import de.mischmaschine.database.sql.network.mysql.AbstractMySQL
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import permission.data.DatabaseConfiguration
import permission.data.mongodb.MongoDB
import permission.data.mongodb.group.PermissionGroupDataMongoDB
import permission.data.mongodb.player.PermissionPlayerMongoDB
import permission.data.sql.MySQL
import permission.data.sql.group.PermissionGroupDataMySQL
import permission.data.sql.player.PermissionPlayerMySQL
import permission.group.manager.PermissionGroupManager
import permission.player.manager.PermissionPlayerManager
import java.io.File

class PermissionInitializer(absolutePath: String) {

    val permissionPlayerManager: PermissionPlayerManager
    val permissionGroupManager: PermissionGroupManager

    init {
        var databaseConfiguration = DatabaseConfiguration(
            "mongodb",
            "permissionPlayer",
            "127.0.0.1",
            27017,
            "username",
            "password"
        )
        val child =
            File(File(absolutePath).also { it.mkdirs() }, "databaseCredentials.json").also { it.createNewFile() }
        val childText = child.readText()
        if (childText.isEmpty()) {
            child.writeText(databaseConfiguration.encodeToString())
        } else {
            val format = Json { prettyPrint = true }
            databaseConfiguration = format.decodeFromString(childText)
        }
        Configuration(
            databaseConfiguration.host,
            databaseConfiguration.port,
            databaseConfiguration.user,
            databaseConfiguration.password,
            when (databaseConfiguration.databaseType.lowercase()) {
                "mongodb" -> AbstractMongoDB::class
                "mysql" -> AbstractMySQL::class
                "sqlite" -> AbstractSQLite::class
                "h2sql" -> AbstractH2SQL::class
                "mariadb" -> AbstractMariaDB::class
                else -> throw IllegalArgumentException(
                    "Unknown database type: ${databaseConfiguration.databaseType}"
                )
            }
        )
        when (databaseConfiguration.databaseType.lowercase()) {
            "mysql" -> {
                val mySQL = MySQL(databaseConfiguration.databaseName)

                this.permissionPlayerManager = PermissionPlayerManager(PermissionPlayerMySQL(mySQL))
                PermissionPlayerManager.instance = permissionPlayerManager

                this.permissionGroupManager = PermissionGroupManager(PermissionGroupDataMySQL(mySQL))
                PermissionGroupManager.instance = permissionGroupManager
            }

            "mongodb" -> {
                val mongoDB = MongoDB()

                this.permissionPlayerManager = PermissionPlayerManager(PermissionPlayerMongoDB(mongoDB))
                PermissionPlayerManager.instance = permissionPlayerManager

                this.permissionGroupManager = PermissionGroupManager(PermissionGroupDataMongoDB(mongoDB))
                PermissionGroupManager.instance = permissionGroupManager
            }

            else -> throw IllegalArgumentException("Unknown database type: ${databaseConfiguration.databaseType}")
        }
    }
}