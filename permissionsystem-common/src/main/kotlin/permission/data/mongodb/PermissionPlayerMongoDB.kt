package permission.data.mongodb

import com.google.gson.Gson
import org.bson.Document
import permission.data.IPermissionPlayerData
import permission.player.PermissionPlayer
import java.util.*
import java.util.concurrent.CompletableFuture

internal class PermissionPlayerMongoDB : IPermissionPlayerData {

    private val mongoDB = MongoDB()

    override fun getPermissionPlayerData(uuid: UUID): CompletableFuture<PermissionPlayer?> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync gson.fromJson(
                mongoDB.getDocumentSync(PERMISSION_PLAYER_COLLECTION, uuid.toString())
                    ?.getString(PERMISSION_PLAYER_DATA), PermissionPlayer::class.java
            )
        }
    }

    override fun setPermissionPlayerData(permissionPlayer: PermissionPlayer) {
        println("Inserting to database...")
        mongoDB.insertDocumentSync(
            PERMISSION_PLAYER_COLLECTION,
            permissionPlayer.uuid.toString(),
            Document(PERMISSION_PLAYER_DATA, gson.toJson(permissionPlayer))
        )
        println("Finished!")
    }

    companion object {
        const val PERMISSION_PLAYER_COLLECTION = "permissionPlayer"
        const val PERMISSION_PLAYER_DATA = "permissionPlayerData"
        val gson: Gson = com.google.gson.GsonBuilder().setPrettyPrinting().serializeNulls().create()
    }

}

