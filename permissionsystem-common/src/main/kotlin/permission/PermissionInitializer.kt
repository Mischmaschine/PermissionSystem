package permission

import de.mischmaschine.database.database.Configuration
import de.mischmaschine.database.mongodb.AbstractMongoDB
import de.mischmaschine.database.sql.local.h2.AbstractH2SQL
import de.mischmaschine.database.sql.local.sqlite.AbstractSQLite
import de.mischmaschine.database.sql.network.mariadb.AbstractMariaDB
import de.mischmaschine.database.sql.network.mysql.AbstractMySQL
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
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

class PermissionInitializer(absoluteFile: File, private val logger: Logger) {

    val permissionPlayerManager: PermissionPlayerManager
    val permissionGroupManager: PermissionGroupManager

    init {
        this.logger.warn(
            "Please note that this is a beta version of PermissionSystem. It is not stable yet."
        )
        this.logger.warn("If you find any bugs, please report them to the github repository.")

        //Creates a new configuration file.
        var databaseConfiguration = DatabaseConfiguration()
        val child = File(absoluteFile.also { it.mkdirs() }, "databaseCredentials.json").also { it.createNewFile() }
        val childText = child.readText()
        val format = Json { prettyPrint = true }
        if (childText.isEmpty()) {
            child.writeText(format.encodeToString(databaseConfiguration))
        } else {
            databaseConfiguration = format.decodeFromString(childText)
        }
        //Loads the database configuration from the file.
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
        //Tries to create new instances of the database. If it fails, it will throw an exception.
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