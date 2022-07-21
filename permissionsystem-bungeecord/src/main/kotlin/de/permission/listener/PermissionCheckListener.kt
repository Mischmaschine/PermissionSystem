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
        player.getCachedPermissionPlayer()?.let {
            event.setHasPermission(it.hasPermission(event.permission))
            CompletableFuture.runAsync {
                it.getPermissions().filter { permission -> permission.isExpired() }
                    .forEach(it::removePermission).also { _ ->
                        it.update()
                    }
            }
        } ?: Logger.getGlobal().log(Level.WARNING, "Permission player from ${player.name} not found")
    }
}