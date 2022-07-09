package de.permission.listener.event

import de.permission.command.PermissionCommand
import de.permission.listener.PermissionCheckListener
import de.permission.listener.PlayerLoginListener
import de.permission.permissionsystem.PermissionSystem
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import permission.player.manager.PermissionPlayerManager

class EventRegistrationService(
    private val permissionSystem: PermissionSystem,
    private val permissionPlayerManager: PermissionPlayerManager,
) {

    init {
        registerListeners(
            PermissionCheckListener(),
            PlayerLoginListener(permissionSystem, permissionPlayerManager)
        )
        registerCommands(PermissionCommand(permissionSystem, permissionPlayerManager))
    }

    private fun registerListeners(vararg listener: Listener) {
        listener.forEach { permissionSystem.proxy.pluginManager.registerListener(permissionSystem, it) }
    }

    private fun registerCommands(vararg command: Command) {
        command.forEach { permissionSystem.proxy.pluginManager.registerCommand(permissionSystem, it) }
    }
}