package permission

import de.mischmaschine.database.database.Configuration
import de.mischmaschine.database.mongodb.AbstractMongoDB
import permission.data.DatabaseConfiguration
import permission.data.mongodb.MongoDB
import permission.data.mongodb.PermissionPlayerMongoDB
import permission.player.manager.PermissionPlayerManager

class PermissionInitializer {

    val permissionPlayerManager: PermissionPlayerManager

    init {
        val databaseConfiguration = DatabaseConfiguration(
            "mongodb",
            "permissionPlayer",
            "179.61.251.243",
            27017,
            "mongoAdmin",
            "llxNAslfGR0NxHPFkzRfrioz8XlfEO6bOUHVxuu26R0G8OJkOc"
        )
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