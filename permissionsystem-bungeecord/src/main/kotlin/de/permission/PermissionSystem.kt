package de.permission

import net.md_5.bungee.api.plugin.Plugin
import permission.PermissionInitializer

class PermissionSystem : Plugin() {

    lateinit var permissionInitializer: PermissionInitializer

    override fun onEnable() {
        this.permissionInitializer = PermissionInitializer()
    }
}