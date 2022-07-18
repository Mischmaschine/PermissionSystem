package de.permission.listener

import de.permission.extensions.getCachedPermissionPlayer
import de.permission.permissionsystem.PermissionSystem
import net.md_5.bungee.api.event.ServerSwitchEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class PlayerSwitchListener(private val permissionSystem: PermissionSystem) : Listener {

    @EventHandler
    fun onSwitch(event: ServerSwitchEvent) {
        println(event.player.server.info.name)
        val player = event.player
        player.getCachedPermissionPlayer()?.let {
            permissionSystem.publishData(player, it)
        }
    }
}