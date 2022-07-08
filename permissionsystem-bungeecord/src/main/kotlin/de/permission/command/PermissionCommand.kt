package de.permission.command

import getCachedPermissionPlayer
import getPermissionPlayer
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor
import permission.Permission
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager

class PermissionCommand(private val permissionPlayerManager: PermissionPlayerManager) : Command("permission"),
    TabExecutor {

    /**
     * This method should set, get or remove a permission to the given player.
     */
    override fun execute(sender: CommandSender, args: Array<out String>) {
        when (args[0]) {
            "remove" -> {
                if (args.size == 3) {
                    val permissionPlayer = ProxyServer.getInstance().getPlayer(args[1]).getPermissionPlayer()
                    permissionPlayer.get()?.removePermission(args[2])
                    return
                }
                sender.sendMessage(TextComponent("§c/permission remove <permission>"))
            }
            "set" -> {
                if (args.size >= 3) {
                    val permissionPlayer = ProxyServer.getInstance().getPlayer(args[1]).getPermissionPlayer()
                    permissionPlayer.get()?.setPermission(Permission(args[2], args[3].toLong()))
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
                        it.addPermission(Permission(args[2], args[3].toLong()))
                        permissionPlayerManager.setPermissionPlayer(it)
                        sender.sendMessage(TextComponent("§aPermission added successfully §l§8| §a${args[2]}§l§8: §a${args[3]}"))
                    }
                        return
                }
                sender.sendMessage(TextComponent("§c/permission add <permission>"))

            }
            "clear" -> {
                if (args.size == 2) {
                    val permissionPlayer = ProxyServer.getInstance().getPlayer(args[1]).getPermissionPlayer()
                    permissionPlayer.get()?.clearPermissions()
                    return
                }
                sender.sendMessage(TextComponent("§c/permission clear"))

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
        }
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>): List<String> {
        return when (args.size) {
            1 -> listOf("remove", "set", "add", "clear", "info")
            2 -> ProxyServer.getInstance().players.map { it.name }.filter { it.startsWith(args[0]) }
            else -> emptyList()
        }
    }

}