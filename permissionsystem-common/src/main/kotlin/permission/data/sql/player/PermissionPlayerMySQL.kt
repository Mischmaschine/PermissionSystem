package permission.data.sql.player

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import permission.data.playerdata.IPermissionPlayerData
import permission.data.sql.MySQL
import permission.future.FutureAction
import permission.player.PermissionPlayer
import java.sql.SQLException
import java.util.*

internal class PermissionPlayerMySQL(private val mySQL: MySQL) : IPermissionPlayerData {

    override fun getPermissionPlayerData(uuid: UUID): FutureAction<PermissionPlayer> {
        return FutureAction {
            executors.submit {
                val resultSet = mySQL.getResultSync(PERMISSION_PLAYER_TABLE, "uuid", uuid.toString()).getResultSet()
                if (resultSet.next()) {
                    resultSet.getString("data")?.let {
                        this.complete(json.decodeFromString(it))
                    } ?: this.completeExceptionally(SQLException("No result found"))
                }
            }
        }
    }

    override fun updatePermissionPlayerData(permissionPlayer: PermissionPlayer) {
        mySQL.updateAsync(
            PERMISSION_PLAYER_TABLE,
            permissionPlayer.uuid.toString(),
            "data",
            json.encodeToString(permissionPlayer)
        )
    }

    override fun setPermissionPlayerData(permissionPlayer: PermissionPlayer) {
        mySQL.insertAsync(
            PERMISSION_PLAYER_TABLE,
            listOf(permissionPlayer.uuid.toString(), json.encodeToString(permissionPlayer)),
        )
    }

    companion object {
        const val PERMISSION_PLAYER_TABLE = "permission_player"
    }
}