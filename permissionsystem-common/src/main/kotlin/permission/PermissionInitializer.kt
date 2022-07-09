package permission

import com.google.gson.GsonBuilder
import de.mischmaschine.database.database.Configuration
import de.mischmaschine.database.mongodb.AbstractMongoDB
import de.mischmaschine.database.sql.local.h2.AbstractH2SQL
import de.mischmaschine.database.sql.local.sqlite.AbstractSQLite
import de.mischmaschine.database.sql.network.mariadb.AbstractMariaDB
import de.mischmaschine.database.sql.network.mysql.AbstractMySQL
import permission.data.DatabaseConfiguration
import permission.data.mongodb.PermissionPlayerMongoDB
import permission.data.sql.PermissionPlayerMySQL
import permission.player.manager.PermissionPlayerManager
import java.io.File

class PermissionInitializer(absolutePath: String) {

    val permissionPlayerManager: PermissionPlayerManager

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
        this.permissionPlayerManager =
            PermissionPlayerManager(
                if (databaseConfiguration.databaseType == "mongodb") {
                    PermissionPlayerMongoDB()
                } else PermissionPlayerMySQL()
            )
        PermissionPlayerManager.instance = permissionPlayerManager
    }
}