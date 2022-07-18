package de.permission.permissionsystem

import com.google.common.io.ByteStreams
import de.permission.BungeeCommandManagerSurrogate
import de.permission.listener.event.EventRegistrationService
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Plugin
import permission.PermissionInitializer

class PermissionSystem : Plugin() {

    override fun onEnable() {
        val permissionInitializer = PermissionInitializer(this.dataFolder.absolutePath)
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

    fun publishData(player: ProxiedPlayer, permissionJson: String) {
        val networkPlayers = ProxyServer.getInstance().players
        networkPlayers?.let {
            if (it.isEmpty()) {
                return
            }
            player.server.info.sendData("player:permsUpdate",
                ByteStreams.newDataOutput().also { dataOutputStream -> dataOutputStream.writeUTF(permissionJson) }
                    .toByteArray()
            )
        }
    }
}