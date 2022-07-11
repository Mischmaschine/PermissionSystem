package de.permissionsystem.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.permission.PermissionsSetupEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import de.permissionsystem.velocity.permission.PermissionProviderSurrogate
import permission.PermissionInitializer
import java.nio.file.Path
import kotlin.io.path.absolutePathString

@Plugin(id = "permissionsystem", name = "PermissionSystem", version = "1.0")
class VelocityPluginMain @Inject constructor(
    private val proxyServer: ProxyServer,
    @DataDirectory private val dataDirectory: Path
) {

    lateinit var permissionProviderSurrogate: PermissionProviderSurrogate

    @Subscribe
    fun onProxyInitialize(event: ProxyInitializeEvent) {
        val permissionInitializer = PermissionInitializer(dataDirectory.absolutePathString())
        this.permissionProviderSurrogate = PermissionProviderSurrogate(permissionInitializer.permissionPlayerManager)
    }

    @Subscribe
    fun onPermissionSetup(event: PermissionsSetupEvent) {
        event.provider = permissionProviderSurrogate
    }
}