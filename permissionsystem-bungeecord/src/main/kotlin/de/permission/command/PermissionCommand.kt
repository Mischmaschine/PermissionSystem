package de.permission.command

import com.google.gson.GsonBuilder
import de.permission.permissionsystem.PermissionSystem
import getPermissionPlayer
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor
import permission.Permission
import permission.group.PermissionGroup
import permission.group.PermissionInfoGroup
import permission.group.manager.PermissionGroupManager
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager
import permission.update

class PermissionCommand(
    private val permissionSystem: PermissionSystem,
    private val permissionPlayerManager: PermissionPlayerManager
) : Command("permission", "permissionsystem.commands.*", "perms"),
    TabExecutor {

    /**
     * This method should set, get or remove a permission to the given player.
     */
    override fun execute(sender: CommandSender, args: Array<out String>) {
        when (args[0]) {
            "remove" -> {
                if (args.size == 3) {
                    val proxiedPlayer = ProxyServer.getInstance().getPlayer(args[1])
                    val permissionPlayer = ProxyServer.getInstance().getPlayer(args[1]).getPermissionPlayer()
                    permissionPlayer.get()?.let {
                        it.removePermission(args[2])
                        it.update()
                        sendCustomData(proxiedPlayer, it)
                    }
                    return
                }
                sender.sendMessage(TextComponent("§c/permission remove <permission>"))
            }
            "set" -> {
                if (args.size == 4) {
                    val proxiedPlayer = ProxyServer.getInstance().getPlayer(args[1])
                    val permissionPlayer = proxiedPlayer.getPermissionPlayer()
                    permissionPlayer.get()?.let {
                        it.setPermission(Permission(args[2], System.currentTimeMillis() + args[3].toLong()))
                        it.update()
                        sendCustomData(proxiedPlayer, it)
                    }
                    return
                }
                sender.sendMessage(TextComponent("§c/permission set <permission>"))

            }
            "add" -> {
                if (args.size == 4) {
                    val player = ProxyServer.getInstance().getPlayer(args[1])
                    val permissionPlayer = player.getPermissionPlayer().get()
                    println(permissionPlayer)
                    permissionPlayer?.let {
                        it.addPermission(Permission(args[2], System.currentTimeMillis() + args[3].toLong()))
                        it.addPermissionInfoGroup(PermissionInfoGroup("test", System.currentTimeMillis() + args[3].toLong()))
                        PermissionGroupManager.instance.createPermissionGroup(PermissionGroup("test", mutableSetOf(Permission("Test", System.currentTimeMillis() + 342834843)), mutableSetOf(), 0))
                        it.update()
                        sendCustomData(player, it)
                        sender.sendMessage(TextComponent("§aPermission added successfully §l§8| §a${args[2]}§l§8: §a${args[3]}"))
                    }
                    return
                }
                sender.sendMessage(TextComponent("§c/permission add <permission>"))

            }
            "clear" -> {
                if (args.size == 2) {
                    val player = ProxyServer.getInstance().getPlayer(args[1])
                    val permissionPlayer = player.getPermissionPlayer()
                    permissionPlayer.get()?.let {
                        it.clearPermissions()
                        it.update()
                        sendCustomData(player, it)
                    }
                    return
                }
                sender.sendMessage(TextComponent("§c/permission clear <Name>"))

            }
            "info" -> {
                if (args.size == 2) {
                    val permissionPlayer = ProxyServer.getInstance().getPlayer(args[1]).getPermissionPlayer().get()
                    permissionPlayer?.getPermissions()?.forEach {
                        sender.sendMessage(TextComponent("§a${it.permissionName} §7- §c${it.timeout}"))
                    }
                    return
                }
                sender.sendMessage(TextComponent("§c/permission info"))

            }
            else -> {
                sender.sendMessage(TextComponent("§c/permission <remove|set|add|clear|info>"))
            }
        }
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>): List<String> {
        return when (args.size) {
            1 -> listOf("remove", "set", "add", "clear", "info")
            2 -> ProxyServer.getInstance().players.map { it.name }.filter { it.startsWith(args[0]) }
            else -> emptyList()
        }
    }

    private fun sendCustomData(proxiedPlayer: ProxiedPlayer, permissionPlayerInstance: PermissionPlayer) {
        permissionSystem.sendCustomData(
            proxiedPlayer,
            gson.toJson(permissionPlayerInstance)
        )
    }

    companion object {
        private val gson = GsonBuilder().serializeNulls().create()
    }

}