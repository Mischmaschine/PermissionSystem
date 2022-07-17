package de.permission.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import de.permission.extensions.getPermissionPlayer
import de.permission.permissionsystem.PermissionSystem
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import permission.Permission
import permission.extensions.update
import permission.group.PermissionGroup
import permission.group.PermissionInfoGroup
import permission.group.manager.PermissionGroupManager

@CommandPermission("permission.command.*")
@CommandAlias("perms|permission")
class PermissionCommand(
    private val permissionSystem: PermissionSystem,
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

    @Subcommand("group delete")
    @Syntax("<name>")
    fun onGroupDelete(commandSender: CommandSender, groupName: String) {
        permissionGroupManager.getPermissionGroup(groupName).onSuccess {
            permissionGroupManager.deletePermissionGroup(groupName)
            commandSender.sendMessage(TextComponent("Group deleted"))
        }.onFailure {
            commandSender.sendMessage(TextComponent("Group does not exist"))
        }
    }

    @Subcommand("user")
    @CommandCompletion("@players permission|group add|remove")
    @Syntax("<player> <permission|group> <add|remove> <groupName|permissionName> <timeout>")
    fun onUser(
        commandSender: CommandSender,
        playerName: String,
        addition: String,
        action: String,
        typeName: String,
        @Optional
        timeout: Long?
    ) {
        val player = permissionSystem.proxy.getPlayer(playerName)
        when (addition.lowercase()) {
            "permission" -> {
                when (action.lowercase()) {
                    "add" -> {
                        player.getPermissionPlayer().onSuccess {
                            timeout?.let { long ->
                                val permission = Permission(typeName, long)
                                it.addPermission(permission)
                                permissionSystem.publishData(player, it.encodeToString())
                                it.update()
                                commandSender.sendMessage(TextComponent("§aPermission added"))
                            } ?: commandSender.sendMessage(TextComponent("§cPlease provide a timeout!"))
                        }.onFailure {
                            commandSender.sendMessage(TextComponent("§cPlayer not found"))
                        }
                    }

                    "remove" -> {
                        player.getPermissionPlayer().onSuccess {
                            it.removePermission(typeName)
                            permissionSystem.publishData(player, it.encodeToString())
                            it.update()
                            commandSender.sendMessage(TextComponent("§aPermission removed"))
                        }.onFailure {
                            commandSender.sendMessage(TextComponent("§cPlayer not found"))
                        }
                    }
                }
            }

            "group" -> {
                when (action.lowercase()) {
                    "add" -> {
                        player.getPermissionPlayer().onSuccess {
                            timeout?.let { long ->
                                val infoGroup = PermissionInfoGroup(typeName, long)
                                it.addPermissionInfoGroup(infoGroup)
                                permissionSystem.publishData(player, it.encodeToString())
                                it.update()
                                commandSender.sendMessage(TextComponent("§aGroup added"))
                            }
                        }.onFailure {
                            commandSender.sendMessage(TextComponent("§cPlayer not found"))
                        }
                    }

                    "remove" -> {
                        player.getPermissionPlayer().onSuccess {
                            it.removePermissionInfoGroup(typeName)
                            permissionSystem.publishData(player, it.encodeToString())
                            it.update()
                            commandSender.sendMessage(TextComponent("§aGroup removed"))
                        }.onFailure {
                            commandSender.sendMessage(TextComponent("§cPlayer not found"))
                        }
                    }
                }
            }
        }
    }
}