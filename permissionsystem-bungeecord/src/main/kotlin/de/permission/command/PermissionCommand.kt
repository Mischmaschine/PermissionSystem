package de.permission.command

import com.google.gson.GsonBuilder
import de.permission.permissionsystem.PermissionSystem
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor
import permission.extensions.delete
import permission.extensions.update
import permission.group.PermissionGroup
import permission.group.manager.PermissionGroupManager
import permission.player.PermissionPlayer

class PermissionCommand(
    private val permissionSystem: PermissionSystem,
    private val permissionGroupManager: PermissionGroupManager
) : Command("permission", "permissionsystem.commands.*", "perms"),
    TabExecutor {

    /**
     * This method should set, get or remove a permission to the given player.
     */
    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (args.isEmpty()) {
            sender.sendMessage(TextComponent("§cUsage: /permission group <group> <add|remove|(inheritance -> add|remove)> <permission/group>"))
            return
        }

        when (args[0].lowercase()) {
            //Groups
            "group" -> {
                if (args.size < 5) {
                    sender.sendMessage(TextComponent("§cUsage: /permission group <group> <add|remove|(inheritance -> add|remove)> <permission/group>"))
                    return
                }
                val group = permissionGroupManager.getPermissionGroup(args[1])
                when (args[2].lowercase()) {
                    "create" -> {

                        group.get()?.let {
                            sender.sendMessage(TextComponent("§cGroup already exists"))
                            return
                        } ?: permissionGroupManager.createPermissionGroup(
                            PermissionGroup(
                                name = args[1],
                                priority = 0
                            ).also {
                                sender.sendMessage(TextComponent("§aGroup §7${it.getName()} §awas successfully created."))
                            }
                        )
                    }

                    "delete" -> {

                        group.whenComplete { nonBlockingGroup, throwable ->
                            if (throwable != null) {
                                sender.sendMessage(TextComponent("§cGroup does not exist"))
                                return@whenComplete
                            }
                            nonBlockingGroup?.delete()
                            sender.sendMessage(TextComponent("§aGroup §7${nonBlockingGroup?.getName()} §awas successfully deleted."))
                        }
                    }

                    "inheritance" -> {
                        when (args[3].lowercase()) {
                            "add" -> {
                                group.get()?.let {
                                    permissionGroupManager.getPermissionGroup(args[4]).get()?.let { permissionGroup ->
                                        it.addInheritance(
                                            permissionGroup
                                        )
                                    } ?: sender.sendMessage(TextComponent("§cTarget group does not exist"))

                                    it.update()
                                    sender.sendMessage(TextComponent("§aGroup §7${it.getName()} §awas successfully added to the inheritance list."))
                                } ?: sender.sendMessage(TextComponent("§cGroup does not exist"))
                            }

                            "remove" -> {
                                group.get()?.let {
                                    it.removeInheritance(
                                        permissionGroupManager.getPermissionGroup(args[4]).get() ?: return
                                    )
                                    it.update()
                                    sender.sendMessage(TextComponent("§aGroup §7${it.getName()} §awas successfully removed from the inheritance list."))
                                } ?: sender.sendMessage(TextComponent("§cGroup does not exist"))
                            }
                        }
                    }
                }
            }
            "user" -> {
                val player = permissionSystem.proxy.getPlayer(args[2])

                player?.let {
                    when (args[3].lowercase()) {

                    }
                } ?: sender.sendMessage(TextComponent("§cPlayer was not found"))
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