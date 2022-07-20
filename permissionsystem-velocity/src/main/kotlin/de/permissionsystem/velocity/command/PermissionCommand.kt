package de.permissionsystem.velocity.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import com.velocitypowered.api.command.CommandSource
import de.permissionsystem.velocity.extenstions.getPermissionPlayer
import de.permissionsystem.velocity.velocityplugin.VelocityPluginMain
import net.kyori.adventure.text.Component
import permission.Permission
import permission.extensions.update
import permission.group.PermissionGroup
import permission.group.PermissionInfoGroup
import permission.group.manager.PermissionGroupManager

@CommandPermission("permission.command.*")
@CommandAlias("perms|permission")
class PermissionCommand(
    private val velocityPluginMain: VelocityPluginMain,
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
    fun onGroupCreate(commandSource: CommandSource, groupName: String) {
        permissionGroupManager.getPermissionGroup(groupName).onSuccess {
            commandSource.sendMessage(Component.text("Group already exists"))
        }.onFailure {
            permissionGroupManager.createPermissionGroup(PermissionGroup(groupName, priority = 0))
            commandSource.sendMessage(Component.text("Group created"))
        }
    }

    @Subcommand("group delete")
    @Syntax("<name>")
    @Description("Deletes a group")
    fun onGroupDelete(commandSource: CommandSource, groupName: String) {
        permissionGroupManager.getPermissionGroup(groupName).onSuccess {
            permissionGroupManager.deletePermissionGroup(groupName)
            commandSource.sendMessage(Component.text("Group deleted"))
        }.onFailure {
            commandSource.sendMessage(Component.text("Group does not exist"))
        }
    }

    @Subcommand("user")
    @CommandCompletion("@players permission|group add|remove")
    @Syntax("<player> <permission|group> <add|remove> <groupName|permissionName> <timeout>")
    @Description("Add or remove a permission or group to a player")
    fun onUser(
        commandSource: CommandSource,
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

        val player = velocityPluginMain.proxyServer.getPlayer(playerName).orElse(null) ?: run {
            commandSource.sendMessage(Component.text("§cPlayer not found"))
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
                                            velocityPluginMain.publishData(player, it)
                                            it.update()
                                            player.sendMessage(Component.text("§aPermission added"))

                                        }.onFailure {
                                            player.sendMessage(Component.text("§cPlayer not found"))
                                        }
                                    }
                                } ?: player.sendMessage(Component.text("§cPlease provide a timeout!"))

                            }

                            "remove" -> {
                                typeName?.let { typeName ->
                                    player.getPermissionPlayer().onSuccess {
                                        it.removePermission(typeName)
                                        velocityPluginMain.publishData(player, it)
                                        it.update()
                                        player.sendMessage(Component.text("§aPermission removed"))
                                    }.onFailure {
                                        player.sendMessage(Component.text("§cPlayer not found"))
                                    }
                                }
                            }

                            else -> {
                                player.sendMessage(Component.text("§cPlease provide a valid action!"))
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
                                            velocityPluginMain.publishData(player, it)
                                            it.update()
                                            player.sendMessage(Component.text("§aGroup added"))

                                        }.onFailure {
                                            player.sendMessage(Component.text("§cPlayer not found"))
                                        }
                                    }
                                }
                            }

                            "remove" -> {
                                typeName?.let { typeName ->
                                    player.getPermissionPlayer().onSuccess {
                                        it.removePermissionInfoGroup(typeName)
                                        velocityPluginMain.publishData(player, it)
                                        it.update()
                                        player.sendMessage(Component.text("§aGroup removed"))
                                    }.onFailure {
                                        player.sendMessage(Component.text("§cPlayer not found"))
                                    }
                                }
                            }

                            else -> {
                                player.sendMessage(Component.text("§cPlease provide a valid action!"))
                            }
                        }
                    }

                }

                else -> {
                    player.sendMessage(Component.text("§cUnknown addition"))
                }
            }
        } ?: run {
            player.getPermissionPlayer().onSuccess {
                player.sendMessage(
                    Component.text(
                        "§a${
                            it.getAllNotExpiredPermissions().map { permission -> permission.name }
                        }"
                    )
                )
            }.onFailure {
                player.sendMessage(Component.text("§cPlayer not found"))
            }
        }

    }
}