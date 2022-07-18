package de.permission.listener

import de.permission.extensions.getPermissionPlayer
import de.permission.permissionsystem.PermissionSystem
import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import permission.extensions.update
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager

internal class PlayerLoginListener(
    private val permissionSystem: PermissionSystem,
    private val permissionPlayerManager: PermissionPlayerManager
) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerLogin(event: LoginEvent) {

        event.registerIntent(permissionSystem)

        event.connection.getPermissionPlayer().onSuccess {
            it.getPermissions().filter { permission -> permission.isExpired() }.forEach(it::removePermission)
                .also { _ ->
                    it.update()
                    event.completeIntent(permissionSystem)
                }
        }.onFailure {
            permissionPlayerManager.createPermissionPlayer(
                PermissionPlayer(event.connection.uniqueId)
            )
            event.completeIntent(permissionSystem)
        }
    }
}