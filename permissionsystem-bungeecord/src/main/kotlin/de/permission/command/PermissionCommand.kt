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
        @Optional
        addition: String?,
        @Optional
        action: String?,
        @Optional
        typeName: String?,
        @Optional
        timeout: Long?
    ) {
        val player = permissionSystem.proxy.getPlayer(playerName) ?: run {
            commandSender.sendMessage(TextComponent("§cPlayer not found"))
            return
        }
        addition?.let { addition ->
            when (addition.lowercase()) {
                "permission" -> {
                    action?.let { action ->
                        when (action.lowercase()) {
                            "add" -> {
                                timeout?.let { long ->
                                    typeName?.let { typeName ->
                                        player.getPermissionPlayer().onSuccess {
                                            val permission = Permission(typeName, long)
                                            it.addPermission(permission)
                                            permissionSystem.publishData(player, it.encodeToString())
                                            it.update()
                                            commandSender.sendMessage(TextComponent("§aPermission added"))

                                        }.onFailure {
                                            commandSender.sendMessage(TextComponent("§cPlayer not found"))
                                        }
                                    }
                                } ?: commandSender.sendMessage(TextComponent("§cPlease provide a timeout!"))

                            }

                            "remove" -> {
                                typeName?.let { typeName ->
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

                            else -> {
                                commandSender.sendMessage(TextComponent("§cPlease provide a valid action!"))
                            }
                        }
                    }
                }

                "group" -> {
                    action?.let { action ->
                        when (action.lowercase()) {
                            "add" -> {
                                typeName?.let { typeName ->
                                    timeout?.let { long ->
                                        player.getPermissionPlayer().onSuccess {
                                            val infoGroup = PermissionInfoGroup(typeName, long)
                                            it.addPermissionInfoGroup(infoGroup)
                                            permissionSystem.publishData(player, it.encodeToString())
                                            it.update()
                                            commandSender.sendMessage(TextComponent("§aGroup added"))

                                        }.onFailure {
                                            commandSender.sendMessage(TextComponent("§cPlayer not found"))
                                        }
                                    }
                                }
                            }

                            "remove" -> {
                                typeName?.let { typeName ->
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

                            else -> {
                                commandSender.sendMessage(TextComponent("§cPlease provide a valid action!"))
                            }
                        }
                    }

                }

                else -> {
                    commandSender.sendMessage(TextComponent("§cUnknown addition"))
                }
            }
        } ?: run {
            player.getPermissionPlayer().onSuccess {
                commandSender.sendMessage(
                    TextComponent(
                        "§a${
                            it.getAllNotExpiredPermissions().map { permission -> permission.name }
                        }"
                    )
                )
            }.onFailure {
                commandSender.sendMessage(TextComponent("§cPlayer not found"))
            }
        }
    }
}