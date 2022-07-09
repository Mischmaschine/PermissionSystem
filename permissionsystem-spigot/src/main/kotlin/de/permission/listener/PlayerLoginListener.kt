package de.permission.listener

import de.permission.PermissibleBaseSurrogate
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent

class PlayerLoginListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onLogin(event: PlayerLoginEvent) {
        val player = event.player
        val version = Bukkit.getServer().javaClass.`package`.name.split(".")[3]
        Class.forName("org.bukkit.craftbukkit.$version.entity.CraftHumanEntity")?.let {
            val field = it.getDeclaredField("perm")
            field.isAccessible = true
            field[player] = PermissibleBaseSurrogate(player)
        } ?: println("Failed to set perm field")
    }
}