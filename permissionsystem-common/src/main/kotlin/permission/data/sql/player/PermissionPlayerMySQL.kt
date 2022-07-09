package permission.data.sql.player

import com.google.gson.Gson
import permission.data.playerdata.IPermissionPlayerData
import permission.data.sql.MySQL
import permission.player.PermissionPlayer
import java.sql.SQLException
import java.util.*
import java.util.concurrent.CompletableFuture

internal class PermissionPlayerMySQL : IPermissionPlayerData {

    private val mySQL = MySQL()

    override fun getPermissionPlayerData(uuid: UUID): CompletableFuture<PermissionPlayer?> {
        return CompletableFuture.supplyAsync {
            val resultSet =
                mySQL.getResultSync(PERMISSION_PLAYER_TABLE, "uuid", uuid.toString())?.getResultSet()
                    ?: throw SQLException(
                        "No result found"
                    )
            if (resultSet.next()) {
                return@supplyAsync gson.fromJson(resultSet.getString("data"), PermissionPlayer::class.java)
            }
            return@supplyAsync null
        }
    }

    override fun updatePermissionPlayerData(permissionPlayer: PermissionPlayer) {
        mySQL.updateAsync(
            PERMISSION_PLAYER_TABLE,
            "uuid", permissionPlayer.uuid.toString(), "data", gson.toJson(permissionPlayer)
        )
    }

    override fun setPermissionPlayerData(permissionPlayer: PermissionPlayer) {
        mySQL.insertAsync(
            PERMISSION_PLAYER_TABLE,
            listOf(permissionPlayer.uuid.toString(), gson.toJson(permissionPlayer)),
        )
    }

    companion object {
        const val PERMISSION_PLAYER_TABLE = "permission_player"
        val gson: Gson = com.google.gson.GsonBuilder().setPrettyPrinting().serializeNulls().create()
    }

}