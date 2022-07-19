package de.permissionsystem.velocity.permission.service

import co.aikar.commands.BaseCommand
import co.aikar.commands.VelocityCommandManager
import com.velocitypowered.api.event.EventManager
import de.permissionsystem.velocity.command.PermissionCommand
import de.permissionsystem.velocity.subscriber.PlayerServerConnectedSubscriber
import de.permissionsystem.velocity.velocityplugin.VelocityPluginMain
import permission.group.manager.PermissionGroupManager
import permission.player.manager.PermissionPlayerManager

class SubscriberRegistrationService(
    private val velocityPluginMain: VelocityPluginMain,
    private val eventManager: EventManager,
    private val velocityCommandManager: VelocityCommandManager,
    permissionPlayerManager: PermissionPlayerManager,
    permissionGroupManager: PermissionGroupManager
) {

    init {
        velocityCommandManager.enableUnstableAPI("help")
        registerSubscriber(PlayerServerConnectedSubscriber(permissionPlayerManager))
        registerCommand(PermissionCommand(velocityPluginMain, permissionGroupManager))

    }

    private fun registerSubscriber(vararg subscriber: Any) {
        subscriber.forEach { eventManager.register(velocityPluginMain, it) }
    }

    private fun registerCommand(vararg subscriber: BaseCommand) {
        subscriber.forEach { velocityCommandManager.registerCommand(it) }
    }

}