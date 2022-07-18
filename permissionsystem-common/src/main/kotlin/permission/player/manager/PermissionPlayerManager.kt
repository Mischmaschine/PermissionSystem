package permission.player.manager

import permission.data.playerdata.IPermissionPlayerData
import permission.future.FutureAction
import permission.player.PermissionPlayer
import java.util.*

class PermissionPlayerManager(private val permissionPlayerData: IPermissionPlayerData) {

    private val permissionPlayers = mutableMapOf<UUID, PermissionPlayer>()

    fun getPermissionPlayer(uuid: UUID): FutureAction<PermissionPlayer> {
        return getCachedPermissionPlayer(uuid)?.let {
            FutureAction(it)
        } ?: permissionPlayerData.getPermissionPlayerData(uuid).also { future ->
            future.onSuccess {
                permissionPlayers[uuid] = it
            }
        }
    }

    fun createPermissionPlayer(permissionPlayer: PermissionPlayer) {
        updatePermissionPlayerCache(permissionPlayer)
        permissionPlayerData.setPermissionPlayerData(permissionPlayer)
    }

    fun updatePermissionPlayer(permissionPlayer: PermissionPlayer) {
        updatePermissionPlayerCache(permissionPlayer)
        permissionPlayerData.updatePermissionPlayerData(permissionPlayer)
    }

    fun updatePermissionPlayerCache(permissionPlayer: PermissionPlayer) {
        this.permissionPlayers[permissionPlayer.uuid] = permissionPlayer
    }

    fun getCachedPermissionPlayer(uuid: UUID): PermissionPlayer? {
        return permissionPlayers[uuid]
    }

    fun getAllCachedPermissionPlayers(): Collection<PermissionPlayer> {
        return permissionPlayers.values
    }

    companion object {
        lateinit var instance: PermissionPlayerManager
    }
}