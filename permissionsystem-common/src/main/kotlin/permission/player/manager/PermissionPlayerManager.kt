package permission.player.manager

import permission.data.playerdata.IPermissionPlayerData
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

    fun getAllCachedPermissionPlayers(): List<PermissionPlayer> {
        return permissionPlayers.values.toList()
    }

    companion object {
        lateinit var instance: PermissionPlayerManager
    }
}