package permission

import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager

fun PermissionPlayer.update() {
    PermissionPlayerManager.instance.updatePermissionPlayer(this)
}

fun PermissionPlayer.updateCache() {
    PermissionPlayerManager.instance.updatePermissionPlayerCache(this)
}