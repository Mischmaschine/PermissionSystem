package de.permission

import org.bukkit.plugin.java.JavaPlugin
import permission.PermissionInitializer

class PermissionSystem : JavaPlugin() {

    lateinit var permissionInitializer: PermissionInitializer

    override fun onEnable() {
        this.permissionInitializer = PermissionInitializer()
    }
}