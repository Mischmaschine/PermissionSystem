package permission

import com.google.gson.GsonBuilder
import de.mischmaschine.database.database.Configuration
import de.mischmaschine.database.mongodb.AbstractMongoDB
import permission.data.DatabaseConfiguration
import permission.data.mongodb.PermissionPlayerMongoDB
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
            AbstractMongoDB::class
        )
        this.permissionPlayerManager =
            PermissionPlayerManager(PermissionPlayerMongoDB()) //TODO make this configurable
        PermissionPlayerManager.instance = permissionPlayerManager
    }
}