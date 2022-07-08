package de.permission.listener

import getCachedPermissionPlayer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PermissionCheckEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class PermissionCheckListener : Listener {

    @EventHandler
    fun onPermissionCheck(event: PermissionCheckEvent) {
        val player = event.sender as ProxiedPlayer
        player.getCachedPermissionPlayer()
            ?.let { event.setHasPermission(it.hasPermission(event.permission)) }
            ?: println("Permission player from ${player.name} not found")
    }
}