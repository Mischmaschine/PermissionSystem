package de.permission.permissionsystem

import com.google.common.io.ByteStreams
import de.permission.BungeeCommandManagerSurrogate
import de.permission.listener.event.EventRegistrationService
import de.permission.player.PermissionPlayer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Plugin
import org.slf4j.LoggerFactory

class BungeeCordPluginMain : Plugin() {

    val json = Json.Default

    override fun onEnable() {
        val permissionInitializer =
            de.permission.PermissionInitializer(this.dataFolder, LoggerFactory.getLogger("Permission"))
        val bungeeCommandManager = BungeeCommandManagerSurrogate(this)
        @Suppress("DEPRECATION")
        bungeeCommandManager.enableUnstableAPI("help")
        EventRegistrationService(
            this,
            permissionInitializer.permissionPlayerManager,
            permissionInitializer.permissionGroupManager,
            bungeeCommandManager
        )
        this.logger.info("PermissionSystem has been enabled")
        proxy.registerChannel("player:permsUpdate")
    }

    fun publishData(player: ProxiedPlayer, permissionPlayer: PermissionPlayer) {
        val networkPlayers = ProxyServer.getInstance().players
        networkPlayers?.let {
            if (it.isEmpty()) {
                return
            }
            player.server.info.sendData("player:permsUpdate",
                ByteStreams.newDataOutput()
                    .also { dataOutputStream -> dataOutputStream.writeUTF(json.encodeToString(permissionPlayer)) }
                    .toByteArray()
            )
        }
    }
}