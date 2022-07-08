package de.permission.listener

import getPermissionPlayer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager

class PlayerLoginListener(private val permissionPlayerManager: PermissionPlayerManager) : Listener {

    @EventHandler
    fun onPlayerLogin(event: LoginEvent) {
        val player = event.connection as ProxiedPlayer
        player.getPermissionPlayer().get() ?: permissionPlayerManager.setPermissionPlayer(
            PermissionPlayer(
                player.uniqueId,
                mutableSetOf(),
                emptyList()
            )
        )
    }
}