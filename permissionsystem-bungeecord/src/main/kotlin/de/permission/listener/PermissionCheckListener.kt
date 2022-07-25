package de.permission.listener

import de.permission.extensions.getCachedPermissionPlayer
import de.permission.extensions.update
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PermissionCheckEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.util.concurrent.CompletableFuture
import java.util.logging.Level
import java.util.logging.Logger

class PermissionCheckListener : Listener {

    @EventHandler
    fun onPermissionCheck(event: PermissionCheckEvent) {
        val player = event.sender as ProxiedPlayer
        player.getCachedPermissionPlayer()?.let { permissionPlayer ->
            event.setHasPermission(permissionPlayer.hasPermission(event.permission))
            CompletableFuture.runAsync {
                permissionPlayer.getPermissions().filter { permission -> permission.isExpired() }
                    .forEach(permissionPlayer::removePermission).also {
                        permissionPlayer.update()
                    }
                permissionPlayer.getPermissionGroups().filter { group -> group.isExpired() }.forEach {
                    permissionPlayer.removePermissionInfoGroup(it.groupName).also {
                        permissionPlayer.update()
                    }
                }
            }
        } ?: Logger.getGlobal().log(Level.WARNING, "Permission player from ${player.name} not found")
    }
}