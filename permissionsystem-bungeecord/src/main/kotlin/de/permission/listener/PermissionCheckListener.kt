package de.permission.listener

import de.permission.extensions.getCachedPermissionPlayer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PermissionCheckEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import permission.extensions.update
import java.util.concurrent.CompletableFuture

class PermissionCheckListener : Listener {

    @EventHandler
    fun onPermissionCheck(event: PermissionCheckEvent) {
        val player = event.sender as ProxiedPlayer
        player.getCachedPermissionPlayer()?.let {
            event.setHasPermission(it.hasPermission(event.permission))
            CompletableFuture.runAsync {
                it.getPermissions().filter { permission -> permission.isExpired() }
                    .forEach(it::removePermission).also { _ ->
                        it.update()
                    }
            }
        } ?: println("Permission player from ${player.name} not found")
    }
}