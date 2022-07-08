package permission.data.mongodb

import com.google.gson.Gson
import org.bson.Document
import permission.data.IPermissionPlayerData
import permission.player.PermissionPlayer
import java.util.*
import java.util.concurrent.CompletableFuture

internal class PermissionPlayerMongoDB(private val mongoDB: MongoDB) : IPermissionPlayerData {

    override fun getPermissionPlayerData(uuid: UUID): CompletableFuture<PermissionPlayer?> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync gson.fromJson(
                mongoDB.getDocumentSync(PERMISSION_PLAYER_COLLECTION, uuid.toString())
                    ?.getString("permission_player_data"), PermissionPlayer::class.java
            )
        }
    }

    override fun setPermissionPlayerData(permissionPlayer: PermissionPlayer) {
        mongoDB.insertDocumentAsync(
            PERMISSION_PLAYER_COLLECTION,
            permissionPlayer.uuid.toString(),
            Document("permission_player_data", permissionPlayer)
        )
    }

    companion object {
        const val PERMISSION_PLAYER_COLLECTION = "permission_player"
        val gson: Gson = com.google.gson.GsonBuilder().setPrettyPrinting().serializeNulls().create()
    }

}

