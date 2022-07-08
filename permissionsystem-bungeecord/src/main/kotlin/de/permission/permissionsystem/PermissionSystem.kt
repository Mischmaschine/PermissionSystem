package de.permission.permissionsystem

import de.permission.listener.event.EventRegistrationService
import net.md_5.bungee.api.plugin.Plugin
import permission.PermissionInitializer

class PermissionSystem : Plugin() {

    override fun onEnable() {
        val permissionInitializer = PermissionInitializer(this.dataFolder.absolutePath)
        EventRegistrationService(this, permissionInitializer.permissionPlayerManager)
    }
}