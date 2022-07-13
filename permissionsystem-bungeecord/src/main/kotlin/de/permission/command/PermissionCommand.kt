package de.permission.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import permission.Permission
import permission.extensions.update
import permission.group.PermissionGroup
import permission.group.PermissionInfoGroup
import permission.group.manager.PermissionGroupManager
import permission.player.manager.PermissionPlayerManager

@CommandPermission("permission.command.*")
@CommandAlias("perms|permission")
class PermissionCommand(
    private val permissionPlayerManager: PermissionPlayerManager,
    private val permissionGroupManager: PermissionGroupManager
) : BaseCommand() {

    @CommandAlias("help")
    @Default
    @HelpCommand
    fun onHelp(commandHelp: CommandHelp) {
        commandHelp.showHelp()
    }

    @Subcommand("group create")
    @Syntax("<name>")
    fun onGroupCreate(commandSender: CommandSender, groupName: String) {
        permissionGroupManager.getPermissionGroup(groupName).onSuccess {
            commandSender.sendMessage(TextComponent("Group already exists"))
        }.onFailure {
            permissionGroupManager.createPermissionGroup(PermissionGroup(groupName, priority = 0))
            commandSender.sendMessage(TextComponent("Group created"))
        }
    }

    @Subcommand("user")
    @Syntax("<target> <permission>")
    @CommandCompletion("@players permission add")
    fun onPermissionAdd(commandSender: CommandSender, playerName: String, permission: Permission) {
        val player = ProxyServer.getInstance().getPlayer(playerName)
        permissionPlayerManager.getPermissionPlayer(player.uniqueId).onSuccess {
            it?.addPermission(permission)
            it?.update()
            commandSender.sendMessage(TextComponent("Permission added"))
        }.onFailure {
            commandSender.sendMessage(TextComponent("Player not found"))
        }
    }

    @Subcommand("user")
    @Syntax("<target> <permission>")
    @CommandCompletion("@players permission remove")
    fun onPermissionRemove(commandSender: CommandSender, playerName: String, permission: Permission) {
        val player = ProxyServer.getInstance().getPlayer(playerName)
        permissionPlayerManager.getPermissionPlayer(player.uniqueId).onSuccess {
            it?.removePermission(permission)
            it?.update()
            commandSender.sendMessage(TextComponent("Permission removed"))
        }.onFailure {
            commandSender.sendMessage(TextComponent("Player not found"))
        }
    }

    @Subcommand("user")
    @Syntax("<target> <group>")
    @CommandCompletion("@players")
    fun onGroupAdd(commandSender: CommandSender, playerName: String, permission: PermissionInfoGroup) {
        val player = ProxyServer.getInstance().getPlayer(playerName)
        permissionPlayerManager.getPermissionPlayer(player.uniqueId).onSuccess {
            it?.addPermissionInfoGroup(permission)
            it?.update()
            commandSender.sendMessage(TextComponent("PermissionGroup added"))
        }.onFailure {
            commandSender.sendMessage(TextComponent("Player not found"))
        }
    }

    @Subcommand("user")
    @Syntax("<target> <group>")
    @CommandCompletion("@players")
    fun onGroupRemove(commandSender: CommandSender, playerName: String, permission: String) {
        val player = ProxyServer.getInstance().getPlayer(playerName)
        permissionPlayerManager.getPermissionPlayer(player.uniqueId).onSuccess {
            it?.removePermissionInfoGroup(permission)
            it?.update()
            commandSender.sendMessage(TextComponent("PermissionGroup removed"))
        }.onFailure {
            commandSender.sendMessage(TextComponent("Player not found"))
        }
    }
}