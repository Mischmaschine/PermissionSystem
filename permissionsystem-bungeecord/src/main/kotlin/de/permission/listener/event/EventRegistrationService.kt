package de.permission.listener.event

import co.aikar.commands.BaseCommand
import co.aikar.commands.BungeeCommandManager
import de.permission.command.PermissionCommand
import de.permission.listener.PermissionCheckListener
import de.permission.listener.PlayerLoginListener
import de.permission.listener.PlayerSwitchListener
import de.permission.permissionsystem.PermissionSystem
import net.md_5.bungee.api.plugin.Listener
import permission.group.manager.PermissionGroupManager
import permission.player.manager.PermissionPlayerManager

class EventRegistrationService(
    private val permissionSystem: PermissionSystem,
    private val permissionPlayerManager: PermissionPlayerManager,
    private val permissionGroupManager: PermissionGroupManager,
    private val bungeeCommandManager: BungeeCommandManager,
) {

    init {
        registerListeners(
            PermissionCheckListener(),
            PlayerLoginListener(permissionSystem, permissionPlayerManager),
            PlayerSwitchListener(permissionSystem)
        )
        //registerCommands(PermissionCommandOld(permissionSystem, permissionGroupManager))
        registerCommands(PermissionCommand(permissionSystem, permissionGroupManager))

    }

    private fun registerListeners(vararg listener: Listener) {
        listener.forEach { permissionSystem.proxy.pluginManager.registerListener(permissionSystem, it) }
    }

    private fun registerCommands(vararg command: BaseCommand) {
        command.forEach { bungeeCommandManager.registerCommand(it) }
    }
}