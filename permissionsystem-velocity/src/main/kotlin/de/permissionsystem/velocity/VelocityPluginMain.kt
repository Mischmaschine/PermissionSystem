package de.permissionsystem.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.permission.PermissionsSetupEvent
import com.velocitypowered.api.event.player.ServerConnectedEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import de.permissionsystem.velocity.permission.PermissionProviderSurrogate
import net.kyori.adventure.text.Component
import permission.PermissionInitializer
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import kotlin.io.path.absolutePathString

@Plugin(id = "permissionsystem", name = "PermissionSystem", version = "1.0")
class VelocityPluginMain @Inject constructor(
    private val proxyServer: ProxyServer,
    @DataDirectory private val dataDirectory: Path
) {

    lateinit var permissionProviderSurrogate: PermissionProviderSurrogate
    lateinit var permissionInitializer: PermissionInitializer

    @Subscribe
    fun onProxyInitialize(event: ProxyInitializeEvent) {
        this.permissionInitializer = PermissionInitializer(dataDirectory.absolutePathString())
        this.permissionProviderSurrogate = PermissionProviderSurrogate(permissionInitializer.permissionPlayerManager)
        proxyServer.scheduler.buildTask(this) {
            println("PermissionSystem: PermissionSystem is now ready!")
            proxyServer.allPlayers.forEach { player -> player.sendMessage(Component.text("PermissionSystem: Permissions loaded")) }
            println(permissionInitializer.permissionPlayerManager.getAllCachedPermissionPlayers())
            println(permissionInitializer.permissionPlayerManager.getAllCachedPermissionPlayers())
            permissionInitializer.permissionPlayerManager.getAllCachedPermissionPlayers().forEach {
                val player = proxyServer.getPlayer(it.uuid)

                player.get().sendMessage(
                    Component.text(
                        "${it.getPermissions()}"
                    )
                )

            }
        }.repeat(2, TimeUnit.SECONDS).schedule()
    }

    @Subscribe
    fun onPermissionSetup(event: PermissionsSetupEvent) {
        event.provider = permissionProviderSurrogate
    }

    @Subscribe
    fun onServerConnected(event: ServerConnectedEvent) {
        permissionInitializer.permissionPlayerManager.getPermissionPlayer(event.player.uniqueId)
    }
}