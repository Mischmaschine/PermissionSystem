package de.permission.player.manager

import de.permission.data.playerdata.IPermissionPlayerData
import de.permission.future.FutureAction
import de.permission.player.PermissionPlayer
import java.util.*

class PermissionPlayerManager(private val permissionPlayerData: IPermissionPlayerData) {

    private val permissionPlayers = mutableMapOf<UUID, PermissionPlayer>()

    /**
     * Gets a [PermissionPlayer] by [UUID]
     * If the permissionPlayer is cached it will be returned, otherwise it will be loaded from the database and then returned
     */
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

    /**
     * This method returns a collection of all cached permission players.
     */
    fun getAllCachedPermissionPlayers(): Collection<PermissionPlayer> {
        return permissionPlayers.values
    }

    companion object {
        lateinit var instance: PermissionPlayerManager
    }
}