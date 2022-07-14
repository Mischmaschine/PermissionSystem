package de.permission.extensions

import org.bukkit.entity.Player
import permission.future.FutureAction
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager

fun Player.getPermissionPlayer(): FutureAction<PermissionPlayer> {
    return PermissionPlayerManager.instance.getPermissionPlayer(this.uniqueId)
}

fun Player.getCachedPermissionPlayer(): PermissionPlayer? {
    return PermissionPlayerManager.instance.getCachedPermissionPlayer(this.uniqueId)
}