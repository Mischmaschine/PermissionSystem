package de.permission.extensions

import net.md_5.bungee.api.connection.PendingConnection
import net.md_5.bungee.api.connection.ProxiedPlayer
import permission.future.FutureAction
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager

fun ProxiedPlayer.getPermissionPlayer(): FutureAction<PermissionPlayer?> {
    return PermissionPlayerManager.instance.getPermissionPlayer(this.uniqueId)
}

fun PendingConnection.getPermissionPlayer(): FutureAction<PermissionPlayer?> {
    return PermissionPlayerManager.instance.getPermissionPlayer(uniqueId)
}

fun ProxiedPlayer.getCachedPermissionPlayer(): PermissionPlayer? {
    return PermissionPlayerManager.instance.getCachedPermissionPlayer(this.uniqueId)
}