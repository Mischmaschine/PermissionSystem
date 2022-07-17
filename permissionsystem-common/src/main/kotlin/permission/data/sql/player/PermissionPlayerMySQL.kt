package permission.data.sql.player

import kotlinx.serialization.decodeFromString
import permission.data.playerdata.IPermissionPlayerData
import permission.data.sql.MySQL
import permission.future.FutureAction
import permission.player.PermissionPlayer
import java.sql.SQLException
import java.util.*

internal class PermissionPlayerMySQL(private val mySQL: MySQL) : IPermissionPlayerData {

    override fun getPermissionPlayerData(uuid: UUID): FutureAction<PermissionPlayer> {
        val future = FutureAction<PermissionPlayer>()

        executors.submit {
            val resultSet =
                mySQL.getResultSync(PERMISSION_PLAYER_TABLE, "uuid", uuid.toString())?.getResultSet()
                    ?: throw SQLException(
                        "No result found"
                    )
            val permissionPlayer: PermissionPlayer? = if (resultSet.next()) {
                json.decodeFromString(resultSet.getString("data"))
            } else {
                null
            }
            permissionPlayer?.let {
                future.complete(it)
            } ?: future.completeExceptionally(SQLException("No result found"))
        }
        return future
    }

    override fun updatePermissionPlayerData(permissionPlayer: PermissionPlayer) {
        mySQL.updateAsync(
            PERMISSION_PLAYER_TABLE,
            "uuid", permissionPlayer.uuid.toString(), "data", permissionPlayer.encodeToString()
        )
    }

    override fun setPermissionPlayerData(permissionPlayer: PermissionPlayer) {
        mySQL.insertAsync(
            PERMISSION_PLAYER_TABLE,
            listOf(permissionPlayer.uuid.toString(), permissionPlayer.encodeToString()),
        )
    }

    companion object {
        const val PERMISSION_PLAYER_TABLE = "permission_player"
    }
}