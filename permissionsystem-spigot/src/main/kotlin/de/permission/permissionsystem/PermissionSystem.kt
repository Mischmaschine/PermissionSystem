package de.permission.permissionsystem

import de.permission.listener.event.EventRegistrationService
import org.bukkit.plugin.java.JavaPlugin
import permission.PermissionInitializer

class PermissionSystem : JavaPlugin() {

    override fun onEnable() {
        val permissionInitializer = PermissionInitializer(this.dataFolder.absolutePath)
        EventRegistrationService(this, permissionInitializer.permissionPlayerManager)

    }
}