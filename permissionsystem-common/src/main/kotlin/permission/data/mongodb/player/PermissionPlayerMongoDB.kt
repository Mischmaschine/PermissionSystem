package permission.data.mongodb.player


import kotlinx.serialization.decodeFromString
import org.bson.Document
import permission.data.mongodb.MongoDB
import permission.data.playerdata.IPermissionPlayerData
import permission.future.FutureAction
import permission.player.PermissionPlayer
import java.util.*

internal class PermissionPlayerMongoDB(private val mongoDB: MongoDB) : IPermissionPlayerData {

    override fun getPermissionPlayerData(uuid: UUID): FutureAction<PermissionPlayer> {
        val futureAction = FutureAction<PermissionPlayer>()

        executors.submit {
            val permissionPlayer: PermissionPlayer? =
                mongoDB.getDocumentSync(PERMISSION_PLAYER_COLLECTION, uuid.toString())
                    ?.let { json.decodeFromString(it.getString(PERMISSION_PLAYER_DATA)) }
            permissionPlayer?.let {
                futureAction.complete(it)
            } ?: futureAction.completeExceptionally(NullPointerException("PermissionPlayer not found"))
        }
        return futureAction
    }

    override fun updatePermissionPlayerData(permissionPlayer: PermissionPlayer) {
        permissionPlayer.run {
            mongoDB.updateDocumentAsync(
                PERMISSION_PLAYER_COLLECTION,
                uuid.toString(),
                Document(PERMISSION_PLAYER_DATA, encodeToString())
            )
        }
    }

    override fun setPermissionPlayerData(permissionPlayer: PermissionPlayer) {
        permissionPlayer.run {
            mongoDB.insertDocumentAsync(
                PERMISSION_PLAYER_COLLECTION,
                uuid.toString(),
                Document(PERMISSION_PLAYER_DATA, encodeToString())
            )
        }
    }

    companion object {
        const val PERMISSION_PLAYER_COLLECTION = "permissionPlayer"
        const val PERMISSION_PLAYER_DATA = "permissionPlayerData"
    }
}

