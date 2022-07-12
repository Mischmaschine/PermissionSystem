package de.permission.listener

import de.permission.extensions.getPermissionPlayer
import de.permission.permissionsystem.PermissionSystem
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

        player.getPermissionPlayer().onSuccess {
            it?.getPermissions()?.filter { permission -> permission.isExpired() }?.forEach(it::removePermission)
                .also { _ ->
                    it?.update()
                }
        }.onFailure {
            permissionPlayerManager.createPermissionPlayer(
                PermissionPlayer(event.connection.uniqueId)
            )
        }
        event.completeIntent(permissionSystem)

    }
}