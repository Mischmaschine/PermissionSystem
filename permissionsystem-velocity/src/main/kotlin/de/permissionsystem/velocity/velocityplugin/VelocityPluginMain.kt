package de.permissionsystem.velocity.velocityplugin

import com.google.common.io.ByteStreams
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.permission.PermissionsSetupEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import de.permissionsystem.velocity.VelocityCommandManagerSurrogate
import de.permissionsystem.velocity.permission.PermissionProviderSurrogate
import de.permissionsystem.velocity.permission.service.SubscriberRegistrationService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import permission.PermissionInitializer
import permission.player.PermissionPlayer
import java.nio.file.Path

@Plugin(id = "permissionsystem", name = "PermissionSystem", version = "1.0")
class VelocityPluginMain @Inject constructor(
    val proxyServer: ProxyServer,
    val logger: Logger,
    @DataDirectory private val dataDirectory: Path
) {

    val json = Json.Default
    lateinit var permissionProviderSurrogate: PermissionProviderSurrogate
    lateinit var permissionInitializer: PermissionInitializer

    @Subscribe
    fun onProxyInitialize(event: ProxyInitializeEvent) {
        instance = this
        this.permissionInitializer = PermissionInitializer(this.dataDirectory.toFile(), this.logger)
        this.permissionProviderSurrogate = PermissionProviderSurrogate()
        val commandManager = VelocityCommandManagerSurrogate(proxyServer, this)
        SubscriberRegistrationService(
            this,
            proxyServer.eventManager,
            commandManager,
            permissionInitializer.permissionPlayerManager,
            permissionInitializer.permissionGroupManager
        )
    }

    @Subscribe
    fun onPermissionSetup(event: PermissionsSetupEvent) {
        event.provider = permissionProviderSurrogate
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