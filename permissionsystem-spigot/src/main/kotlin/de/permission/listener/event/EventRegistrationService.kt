package de.permission.listener.event

import de.permission.listener.PlayerLoginListener
import de.permission.permissionsystem.PermissionSystem
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import permission.player.manager.PermissionPlayerManager

class EventRegistrationService(
    private val permissionSystem: PermissionSystem,
    private val permissionPlayerManager: PermissionPlayerManager
) {

    init {

        registerListeners(PlayerLoginListener(permissionSystem, permissionPlayerManager))

    }

    fun registerListeners(vararg listener: Listener) {
        listener.forEach { Bukkit.getPluginManager().registerEvents(it, permissionSystem) }
    }
}