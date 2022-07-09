package permission.data.mongodb.player

import org.bson.Document
import permission.data.playerdata.IPermissionPlayerData
import permission.data.mongodb.MongoDB
import permission.player.PermissionPlayer
import java.util.*
import java.util.concurrent.CompletableFuture

internal class PermissionPlayerMongoDB(private val mongoDB: MongoDB) : IPermissionPlayerData {

    override fun getPermissionPlayerData(uuid: UUID): CompletableFuture<PermissionPlayer?> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync gson.fromJson(
                mongoDB.getDocumentSync(PERMISSION_PLAYER_COLLECTION, uuid.toString())
                    ?.getString(PERMISSION_PLAYER_DATA), PermissionPlayer::class.java
            )
        }
    }

    override fun updatePermissionPlayerData(permissionPlayer: PermissionPlayer) {
        mongoDB.updateDocumentAsync(
            PERMISSION_PLAYER_COLLECTION,
            permissionPlayer.uuid.toString(),
            Document(PERMISSION_PLAYER_DATA, gson.toJson(permissionPlayer))
        )
    }

    override fun setPermissionPlayerData(permissionPlayer: PermissionPlayer) {
        mongoDB.insertDocumentAsync(
            PERMISSION_PLAYER_COLLECTION,
            permissionPlayer.uuid.toString(),
            Document(PERMISSION_PLAYER_DATA, gson.toJson(permissionPlayer))
        )
    }

    companion object {
        const val PERMISSION_PLAYER_COLLECTION = "permissionPlayer"
        const val PERMISSION_PLAYER_DATA = "permissionPlayerData"
    }
}

