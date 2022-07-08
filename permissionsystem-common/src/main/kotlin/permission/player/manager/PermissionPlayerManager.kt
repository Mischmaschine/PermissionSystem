package permission.player.manager

import permission.data.IPermissionPlayerData
import permission.player.PermissionPlayer
import java.util.*
import java.util.concurrent.CompletableFuture

class PermissionPlayerManager(private val permissionPlayerData: IPermissionPlayerData) {

    private val permissionPlayers = mutableMapOf<UUID, PermissionPlayer>()

    fun getPermissionPlayer(uuid: UUID): CompletableFuture<PermissionPlayer?> {
        return getCachedPermissionPlayer(uuid)?.let {
            CompletableFuture.completedFuture(it)
        } ?: permissionPlayerData.getPermissionPlayerData(uuid).thenApply {
            it?.let {
                permissionPlayers[it.uuid] = it
                it
            }
        }
    }

    fun setPermissionPlayer(permissionPlayer: PermissionPlayer) {
        this.permissionPlayers[permissionPlayer.uuid] = permissionPlayer
        permissionPlayerData.setPermissionPlayerData(permissionPlayer)
    }

    fun getCachedPermissionPlayer(uuid: UUID): PermissionPlayer? {
        return permissionPlayers[uuid]
    }

    fun getAllCachedPermissionPlayers(): List<PermissionPlayer> {
        return permissionPlayers.values.toList()
    }

    companion object {
        @Deprecated("Don't use a singleton of this class!!")
        lateinit var instance: PermissionPlayerManager
    }

}