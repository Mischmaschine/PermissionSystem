package de.permission.listener

import de.permission.permissionsystem.PermissionSystem
import getPermissionPlayer
import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import permission.extensions.update
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager

class PlayerLoginListener(
    private val permissionSystem: PermissionSystem,
    private val permissionPlayerManager: PermissionPlayerManager
) : Listener {

    @EventHandler
    fun onPlayerLogin(event: LoginEvent) {
        val player = event.connection

        event.registerIntent(permissionSystem)

        val permissionPlayer = player.getPermissionPlayer().get()
        permissionPlayer?.let {
            it.getPermissions().filter { permission -> permission.isExpired() }.forEach(it::removePermission).also {
                permissionPlayer.update()
            }
        } ?: permissionPlayerManager.createPermissionPlayer(
            PermissionPlayer(event.connection.uniqueId)
        )
        event.completeIntent(permissionSystem)

    }
}