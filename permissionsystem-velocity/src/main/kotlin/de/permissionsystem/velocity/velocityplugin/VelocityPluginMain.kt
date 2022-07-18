package de.permissionsystem.velocity.velocityplugin

import com.google.common.io.ByteStreams
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.permission.PermissionsSetupEvent
import com.velocitypowered.api.event.player.ServerConnectedEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import de.permissionsystem.velocity.VelocityCommandManagerSurrogate
import de.permissionsystem.velocity.command.PermissionCommand
import de.permissionsystem.velocity.permission.PermissionProviderSurrogate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import permission.PermissionInitializer
import permission.player.PermissionPlayer
import java.nio.file.Path
import kotlin.io.path.absolutePathString

@Plugin(id = "permissionsystem", name = "PermissionSystem", version = "1.0")
class VelocityPluginMain @Inject constructor(
    val proxyServer: ProxyServer,
    @DataDirectory private val dataDirectory: Path
) {

    val json = Json.Default
    lateinit var permissionProviderSurrogate: PermissionProviderSurrogate
    lateinit var permissionInitializer: PermissionInitializer

    @Subscribe
    fun onProxyInitialize(event: ProxyInitializeEvent) {
        instance = this
        this.permissionInitializer = PermissionInitializer(dataDirectory.absolutePathString())
        this.permissionProviderSurrogate = PermissionProviderSurrogate(permissionInitializer.permissionPlayerManager)
        val commandManager = VelocityCommandManagerSurrogate(proxyServer, this)
        commandManager.enableUnstableAPI("help")
        commandManager.registerCommand(PermissionCommand(this, permissionInitializer.permissionGroupManager))
    }

    @Subscribe
    fun onPermissionSetup(event: PermissionsSetupEvent) {
        event.provider = permissionProviderSurrogate
    }

    @Subscribe
    fun onServerConnected(event: ServerConnectedEvent) {
        permissionInitializer.permissionPlayerManager.getPermissionPlayer(event.player.uniqueId)
    }

    fun publishData(player: Player, permissionPlayer: PermissionPlayer) {
        proxyServer.allPlayers?.let {
            if (it.isEmpty()) {
                return
            }
            player.sendPluginMessage(
                { "player:permsUpdate" },
                ByteStreams.newDataOutput()
                    .also { dataOutputStream -> dataOutputStream.writeUTF(json.encodeToString(permissionPlayer)) }
                    .toByteArray()
            )
        }
    }

    companion object {
        @JvmStatic
        lateinit var instance: VelocityPluginMain
    }
}