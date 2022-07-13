package de.permission.permissionsystem

import com.google.common.io.ByteStreams
import com.google.gson.Gson
import de.permission.listener.event.EventRegistrationService
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener
import permission.PermissionInitializer
import permission.extensions.updateCache
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager

class PermissionSystem : JavaPlugin(), PluginMessageListener {

    lateinit var permissionPlayerManager: PermissionPlayerManager
    override fun onEnable() {
        val permissionInitializer = PermissionInitializer(this.dataFolder.absolutePath)
        this.permissionPlayerManager = permissionInitializer.permissionPlayerManager
        EventRegistrationService(this, permissionInitializer.permissionPlayerManager)
        server.messenger.registerIncomingPluginChannel(this, "player:permsUpdate", this)
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, Runnable {
            permissionPlayerManager.getAllCachedPermissionPlayers().forEach {
                Bukkit.broadcastMessage(
                    "Permission: ${
                        it.getPermissions().map { permission -> permission.permissionName }
                    }"
                )
                Bukkit.broadcastMessage(
                    "Permission: ${
                        it.getPermissions().map { permission -> permission.getRemainingTime() }
                    }"
                )
            }
        }, 0, 45)
    }

    override fun onPluginMessageReceived(channel: String, player: Player, bytes: ByteArray) {
        if (!channel.equals("player:permsUpdate", ignoreCase = true)) {
            return
        }
        ByteStreams.newDataInput(bytes).let {
            val permissionPlayerJson = it.readUTF()
            println(permissionPlayerJson)
            val permissionPlayer = Gson().fromJson(permissionPlayerJson, PermissionPlayer::class.java)
            permissionPlayer.updateCache()
        }
    }
}