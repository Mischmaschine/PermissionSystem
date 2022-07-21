package de.permission.extensions

import de.permission.future.FutureAction
import de.permission.player.PermissionPlayer
import de.permission.player.manager.PermissionPlayerManager
import net.md_5.bungee.api.connection.PendingConnection
import net.md_5.bungee.api.connection.ProxiedPlayer

fun ProxiedPlayer.getPermissionPlayer(): FutureAction<PermissionPlayer> {
    return PermissionPlayerManager.instance.getPermissionPlayer(this.uniqueId)
}

fun PendingConnection.getPermissionPlayer(): FutureAction<PermissionPlayer> {
    return PermissionPlayerManager.instance.getPermissionPlayer(uniqueId)
}

fun ProxiedPlayer.getCachedPermissionPlayer(): PermissionPlayer? {
    return PermissionPlayerManager.instance.getCachedPermissionPlayer(this.uniqueId)
}