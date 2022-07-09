package permission

import com.google.gson.GsonBuilder
import de.mischmaschine.database.database.Configuration
import de.mischmaschine.database.mongodb.AbstractMongoDB
import de.mischmaschine.database.sql.local.h2.AbstractH2SQL
import de.mischmaschine.database.sql.local.sqlite.AbstractSQLite
import de.mischmaschine.database.sql.network.mariadb.AbstractMariaDB
import de.mischmaschine.database.sql.network.mysql.AbstractMySQL
import permission.data.DatabaseConfiguration
import permission.data.mongodb.MongoDB
import permission.data.mongodb.group.PermissionGroupDataMongoDB
import permission.data.mongodb.player.PermissionPlayerMongoDB
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
        val gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()
        if (child.readText().isEmpty()) {
            child.writeText(gson.toJson(databaseConfiguration))
        } else {
            databaseConfiguration = gson.fromJson(child.readText(), DatabaseConfiguration::class.java)
        }
        Configuration(
            databaseConfiguration.host,
            databaseConfiguration.port,
            databaseConfiguration.user,
            databaseConfiguration.password,
            when (databaseConfiguration.databaseType) {
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
        val mongoDB = MongoDB()

        this.permissionPlayerManager =
            PermissionPlayerManager(
                if (databaseConfiguration.databaseType == "mongodb") {
                    PermissionPlayerMongoDB(mongoDB)
                } else PermissionPlayerMySQL()
            )
        PermissionPlayerManager.instance = permissionPlayerManager

        this.permissionGroupManager =
            PermissionGroupManager(
                if (databaseConfiguration.databaseType == "mongodb") {
                    PermissionGroupDataMongoDB(mongoDB)
                } else PermissionGroupDataMongoDB(mongoDB)
            )
        PermissionGroupManager.instance = permissionGroupManager

    }
}