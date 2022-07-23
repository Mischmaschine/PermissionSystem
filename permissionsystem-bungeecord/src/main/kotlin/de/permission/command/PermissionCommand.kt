package de.permission.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import de.permission.extensions.getPermissionPlayer
import de.permission.extensions.sendMessage
import de.permission.extensions.update
import de.permission.group.PermissionGroup
import de.permission.group.PermissionInfoGroup
import de.permission.group.manager.PermissionGroupManager
import de.permission.permissionsystem.BungeeCordPluginMain
import net.md_5.bungee.api.CommandSender

@CommandPermission("permission.command.*")
@CommandAlias("perms|permission")
class PermissionCommand(
    private val BungeeCordPluginMain: BungeeCordPluginMain,
    private val permissionGroupManager: PermissionGroupManager
) : BaseCommand() {

    @CommandAlias("help")
    @HelpCommand
    @Description("Shows help for the permission command")
    fun onHelp(commandHelp: CommandHelp) {
        commandHelp.showHelp()
    }

    @Subcommand("group create")
    @Syntax("<name>")
    @Description("Create a new group")
    fun onGroupCreate(commandSender: CommandSender, groupName: String) {
        permissionGroupManager.getPermissionGroup(groupName).onSuccess {
            commandSender.sendMessage(listOf("Group already exists"))
        }.onFailure {
            permissionGroupManager.createPermissionGroup(PermissionGroup(groupName, priority = 0))
            commandSender.sendMessage(listOf("Group created"))
        }
    }

    @Subcommand("group delete")
    @Syntax("<name>")
    @Description("Deletes a group")
    fun onGroupDelete(commandSender: CommandSender, groupName: String) {
        permissionGroupManager.getPermissionGroup(groupName).onSuccess {
            permissionGroupManager.deletePermissionGroup(groupName)
            commandSender.sendMessage(listOf("Group deleted"))
        }.onFailure {
            commandSender.sendMessage(listOf("Group does not exist"))
        }
    }

    @Subcommand("user")
    @CommandCompletion("@players permission|group add|remove")
    @Syntax("<player> <permission|group> <add|remove> <groupName|permissionName> <timeout>")
    @Description("Add or remove a permission or group to a player")
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
        val player = BungeeCordPluginMain.proxy.getPlayer(playerName) ?: run {
            commandSender.sendMessage(listOf("§cPlayer not found"))
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
                                            val permission = de.permission.Permission(typeName, long)
                                            it.addPermission(permission)
                                            BungeeCordPluginMain.publishData(player, it)
                                            it.update()
                                            commandSender.sendMessage(listOf("§aPermission added"))

                                        }.onFailure {
                                            commandSender.sendMessage(listOf("§cPlayer not found"))
                                        }
                                    }
                                } ?: commandSender.sendMessage(listOf("§cPlease provide a timeout!"))

                            }

                            "remove" -> {
                                typeName?.let { typeName ->
                                    player.getPermissionPlayer().onSuccess {
                                        it.removePermission(typeName)
                                        BungeeCordPluginMain.publishData(player, it)
                                        it.update()
                                        commandSender.sendMessage(listOf("§aPermission removed"))
                                    }.onFailure {
                                        commandSender.sendMessage(listOf("§cPlayer not found"))
                                    }
                                }
                            }

                            else -> {
                                commandSender.sendMessage(listOf("§cPlease provide a valid action!"))
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
                                            BungeeCordPluginMain.publishData(player, it)
                                            it.update()
                                            commandSender.sendMessage(listOf("§aGroup added"))

                                        }.onFailure {
                                            commandSender.sendMessage(listOf("§cPlayer not found"))
                                        }
                                    }
                                }
                            }

                            "remove" -> {
                                typeName?.let { typeName ->
                                    player.getPermissionPlayer().onSuccess {
                                        it.removePermissionInfoGroup(typeName)
                                        BungeeCordPluginMain.publishData(player, it)
                                        it.update()
                                        commandSender.sendMessage(listOf("§aGroup removed"))
                                    }.onFailure {
                                        commandSender.sendMessage(listOf("§cPlayer not found"))
                                    }
                                }
                            }

                            else -> {
                                commandSender.sendMessage(listOf("§cPlease provide a valid action!"))
                            }
                        }
                    }

                }

                else -> {
                    commandSender.sendMessage(listOf("§cUnknown addition"))
                }
            }
        } ?: run {
            player.getPermissionPlayer().onSuccess {
                commandSender.sendMessage(
                    listOf(
                        "§a${
                            it.getAllNotExpiredPermissions().map { permission -> permission.name }
                        }"
                    )
                )
            }.onFailure {
                commandSender.sendMessage(listOf("§cPlayer not found"))
            }
        }
    }
}