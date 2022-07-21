package de.permission.listener.event

import de.permission.listener.PlayerLoginListener
import de.permission.permissionsystem.SpigotPluginMain
import de.permission.player.manager.PermissionPlayerManager
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class EventRegistrationService(
    private val spigotPluginMain: SpigotPluginMain,
    private val permissionPlayerManager: PermissionPlayerManager
) {

    init {

        registerListeners(PlayerLoginListener())

    }

    fun registerListeners(vararg listener: Listener) {
        listener.forEach { Bukkit.getPluginManager().registerEvents(it, spigotPluginMain) }
    }
}