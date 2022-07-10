package permission.data.mongodb.player

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.bson.Document
import permission.data.mongodb.MongoDB
import permission.data.playerdata.IPermissionPlayerData
import permission.player.PermissionPlayer
import java.util.*
import java.util.concurrent.CompletableFuture

internal class PermissionPlayerMongoDB(private val mongoDB: MongoDB) : IPermissionPlayerData {

    override fun getPermissionPlayerData(uuid: UUID): CompletableFuture<PermissionPlayer?> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync mongoDB.getDocumentSync(PERMISSION_PLAYER_COLLECTION, uuid.toString())
                ?.getString(PERMISSION_PLAYER_DATA)?.let {
                    json.decodeFromString(
                        it
                    )
                }
        }
    }

    override fun updatePermissionPlayerData(permissionPlayer: PermissionPlayer) {
        mongoDB.updateDocumentAsync(
            PERMISSION_PLAYER_COLLECTION,
            permissionPlayer.uuid.toString(),
            Document(PERMISSION_PLAYER_DATA, json.encodeToString(permissionPlayer))
        )
    }

    override fun setPermissionPlayerData(permissionPlayer: PermissionPlayer) {
        mongoDB.insertDocumentAsync(
            PERMISSION_PLAYER_COLLECTION,
            permissionPlayer.uuid.toString(),
            Document(PERMISSION_PLAYER_DATA, json.encodeToString(permissionPlayer))
        )
    }

    companion object {
        const val PERMISSION_PLAYER_COLLECTION = "permissionPlayer"
        const val PERMISSION_PLAYER_DATA = "permissionPlayerData"
    }
}

