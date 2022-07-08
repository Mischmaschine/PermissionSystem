package de.permission.listener

import de.permission.permissionsystem.PermissionSystem
import getPermissionPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import permission.player.manager.PermissionPlayerManager

class PlayerLoginListener(
    private val permissionSystem: PermissionSystem,
    private val permissionPlayerManager: PermissionPlayerManager
) : Listener {

    @EventHandler
    fun onJoin(event: PlayerLoginEvent) {
        val player = event.player
        player.getPermissionPlayer(permissionPlayerManager).get()?.let { permissionPlayer ->
            permissionPlayer.getPermissions().forEach {
                player.addAttachment(permissionSystem, it.permissionName, true)
            }
        }
    }
}