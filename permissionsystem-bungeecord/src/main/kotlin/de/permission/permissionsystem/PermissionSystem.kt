package de.permission.permissionsystem

import com.google.common.io.ByteStreams
import de.permission.listener.event.EventRegistrationService
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Plugin
import permission.PermissionInitializer


class PermissionSystem : Plugin() {

    override fun onEnable() {
        val permissionInitializer = PermissionInitializer(this.dataFolder.absolutePath)
        EventRegistrationService(this, permissionInitializer.permissionPlayerManager)
        proxy.registerChannel("player:permsUpdate")
    }

    fun sendCustomData(player: ProxiedPlayer, permissionJson: String) {
        val networkPlayers = ProxyServer.getInstance().players
        // perform a check to see if globally are no players
        if (networkPlayers == null || networkPlayers.isEmpty()) {
            return
        }
        val out = ByteStreams.newDataOutput()
        out.writeUTF(permissionJson)

        // we send the data to the server
        // using ServerInfo the packet is being queued if there are no players in the server
        // using only the server to send data the packet will be lost if no players are in it
        player.server.info.sendData("player:permsUpdate", out.toByteArray())
    }

}