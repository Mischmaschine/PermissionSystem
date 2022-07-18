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
    //@Default
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
            commandSender.sendMessage(TextComponent("Group already exists"))
        }.onFailure {
            permissionGroupManager.createPermissionGroup(PermissionGroup(groupName, priority = 0))
            commandSender.sendMessage(TextComponent("Group created"))
        }
    }

    @Subcommand("group delete")
    @Syntax("<name>")
    @Description("Deletes a group")
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
        val player = permissionSystem.proxy.getPlayer(playerName) ?: run {
            commandSender.sendMessage(TextComponent("§cPlayer not found"))
            return
        }
        println(playerName)
        println(addition)
        println(action)
        println(typeName)
        println(timeout)
        addition?.let { addition ->
            when (addition.lowercase()) {
                "permission" -> {
                    action?.let { action ->
                        when (action.lowercase()) {
                            "add" -> {
                                println("add123")
                                timeout?.let { long ->
                                    println("timeout1234")
                                    typeName?.let { typeName ->
                                        println("typeNameaqe23423")
                                        player.getPermissionPlayer().onSuccess {
                                            println("success")
                                            val permission = Permission(typeName, long)
                                            println("success132")
                                            it.addPermission(permission)
                                            println("succes345345üüüüüüüüüüüs")
                                            permissionSystem.publishData(player, it)
                                            println("successpubluishads")
                                            it.update()
                                            println("updated")
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
                                        permissionSystem.publishData(player, it)
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
                                            permissionSystem.publishData(player, it)
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
                                        permissionSystem.publishData(player, it)
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