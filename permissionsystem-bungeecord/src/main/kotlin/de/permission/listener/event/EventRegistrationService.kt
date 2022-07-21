package de.permission.listener.event

import co.aikar.commands.BaseCommand
import co.aikar.commands.BungeeCommandManager
import de.permission.command.PermissionCommand
import de.permission.group.manager.PermissionGroupManager
import de.permission.listener.PermissionCheckListener
import de.permission.listener.PlayerLoginListener
import de.permission.listener.PlayerSwitchListener
import de.permission.permissionsystem.BungeeCordPluginMain
import de.permission.player.manager.PermissionPlayerManager
import net.md_5.bungee.api.plugin.Listener

class EventRegistrationService(
    private val BungeeCordPluginMain: BungeeCordPluginMain,
    private val permissionPlayerManager: PermissionPlayerManager,
    private val permissionGroupManager: PermissionGroupManager,
    private val bungeeCommandManager: BungeeCommandManager,
) {

    init {
        registerListeners(
            PermissionCheckListener(),
            PlayerLoginListener(BungeeCordPluginMain, permissionPlayerManager),
            PlayerSwitchListener(BungeeCordPluginMain)
        )
        //registerCommands(PermissionCommandOld(permissionSystem, permissionGroupManager))
        registerCommands(PermissionCommand(BungeeCordPluginMain, permissionGroupManager))

    }

    private fun registerListeners(vararg listener: Listener) {
        listener.forEach { BungeeCordPluginMain.proxy.pluginManager.registerListener(BungeeCordPluginMain, it) }
    }

    private fun registerCommands(vararg command: BaseCommand) {
        command.forEach { bungeeCommandManager.registerCommand(it) }
    }
}