package de.permission.permissionsystem

import com.google.common.io.ByteStreams
import de.permission.listener.event.EventRegistrationService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener
import org.slf4j.LoggerFactory
import permission.PermissionInitializer
import permission.extensions.updateCache
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager
import java.util.logging.Level

class SpigotPluginMain : JavaPlugin(), PluginMessageListener {

    lateinit var permissionPlayerManager: PermissionPlayerManager
    override fun onEnable() {
        val permissionInitializer =
            PermissionInitializer(this.dataFolder.absolutePath, LoggerFactory.getLogger("Permission"))
        this.permissionPlayerManager = permissionInitializer.permissionPlayerManager
        EventRegistrationService(this, permissionInitializer.permissionPlayerManager)
        server.messenger.registerIncomingPluginChannel(this, "player:permsUpdate", this)

        this.logger.log(Level.WARNING, "This plugin is only usable in combination with a proxy e.g. BungeeCord.")
    }

    override fun onPluginMessageReceived(channel: String, player: Player, bytes: ByteArray) {
        if (!channel.equals("player:permsUpdate", ignoreCase = true)) {
            return
        }
        ByteStreams.newDataInput(bytes).let {
            val permissionPlayerJson = it.readUTF()
            println(permissionPlayerJson)
            val permissionPlayer = Json.decodeFromString<PermissionPlayer>(permissionPlayerJson)
            permissionPlayer.updateCache()
        }
    }
}