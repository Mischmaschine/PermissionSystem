package de.permission.extensions

import de.permission.player.PermissionPlayer
import de.permission.player.manager.PermissionPlayerManager

fun PermissionPlayer.update() {
    PermissionPlayerManager.instance.updatePermissionPlayer(this)
}

fun PermissionPlayer.updateCache() {
    PermissionPlayerManager.instance.updatePermissionPlayerCache(this)
}