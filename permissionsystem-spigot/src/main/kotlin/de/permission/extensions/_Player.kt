package de.permission.extensions

import de.permission.future.FutureAction
import de.permission.player.PermissionPlayer
import de.permission.player.manager.PermissionPlayerManager
import org.bukkit.entity.Player

fun Player.getPermissionPlayer(): FutureAction<PermissionPlayer> {
    return PermissionPlayerManager.instance.getPermissionPlayer(this.uniqueId)
}

fun Player.getCachedPermissionPlayer(): PermissionPlayer? {
    return PermissionPlayerManager.instance.getCachedPermissionPlayer(this.uniqueId)
}