package de.permission.listener

import de.permission.extensions.getPermissionPlayer
import de.permission.permissionsystem.BungeeCordPluginMain
import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import permission.extensions.update
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager

internal class PlayerLoginListener(
    private val BungeeCordPluginMain: BungeeCordPluginMain,
    private val permissionPlayerManager: PermissionPlayerManager
) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerLogin(event: LoginEvent) {

        event.registerIntent(BungeeCordPluginMain)

        event.connection.getPermissionPlayer().onSuccess {
            it.getPermissions().filter { permission -> permission.isExpired() }.forEach(it::removePermission)
                .also { _ ->
                    it.update()
                    event.completeIntent(BungeeCordPluginMain)
                }
        }.onFailure {
            permissionPlayerManager.createPermissionPlayer(
                PermissionPlayer(event.connection.uniqueId)
            )
            event.completeIntent(BungeeCordPluginMain)
        }
    }
}