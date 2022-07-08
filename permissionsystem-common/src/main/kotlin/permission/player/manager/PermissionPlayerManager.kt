package permission.player.manager

import permission.data.IPermissionPlayerData
import permission.player.PermissionPlayer
import java.util.*
import java.util.concurrent.CompletableFuture

class PermissionPlayerManager(private val permissionPlayerData: IPermissionPlayerData) {

    fun getPermissionPlayer(uuid: UUID): CompletableFuture<PermissionPlayer?> {
        return permissionPlayerData.getPermissionPlayerData(uuid)
    }
}