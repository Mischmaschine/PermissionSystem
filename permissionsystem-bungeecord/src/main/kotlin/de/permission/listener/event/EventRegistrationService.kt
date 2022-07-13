package de.permission.listener.event

import co.aikar.commands.BungeeCommandManager
import co.aikar.commands.contexts.ContextResolver
import de.permission.command.PermissionCommand
import de.permission.listener.PermissionCheckListener
import de.permission.listener.PlayerLoginListener
import de.permission.permissionsystem.PermissionSystem
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import permission.Permission
import permission.group.PermissionInfoGroup
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
            PlayerLoginListener(permissionSystem, permissionPlayerManager)
        )
        //registerCommands(PermissionCommandOld(permissionSystem, permissionGroupManager))

        initContexts()
        bungeeCommandManager.registerCommand(PermissionCommand(permissionPlayerManager, permissionGroupManager))

    }

    private fun registerListeners(vararg listener: Listener) {
        listener.forEach { permissionSystem.proxy.pluginManager.registerListener(permissionSystem, it) }
    }

    private fun registerCommands(vararg command: Command) {
        command.forEach { permissionSystem.proxy.pluginManager.registerCommand(permissionSystem, it) }
    }

    fun initContexts() {
        with(bungeeCommandManager.commandContexts) {
            registerContext(
                Permission::class.java,
                ContextResolver {
                    it.sender
                    println(it.args)
                    val input = it.popFirstArg()
                    val timeout = it.popFirstArg().toLongOrNull() ?: 0
                    return@ContextResolver Permission(input, timeout)
                })
            registerContext(
                PermissionInfoGroup::class.java,
                ContextResolver {
                    it.sender
                    println(it.args)
                    val input = it.popFirstArg()
                    val timeout = it.popFirstArg().toLongOrNull() ?: 0
                    return@ContextResolver PermissionInfoGroup(input, timeout)
                }
            )
        }
    }
}