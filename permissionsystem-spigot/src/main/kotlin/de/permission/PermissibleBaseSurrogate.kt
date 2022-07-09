package de.permission

import getCachedPermissionPlayer
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissibleBase
import org.bukkit.permissions.Permission

class PermissibleBaseSurrogate(private val player: Player) : PermissibleBase(player) {

    override fun hasPermission(permission: String): Boolean {
        if (permission == "bukkit.broadcast.user") {
            return true
        }
        return player.getCachedPermissionPlayer()?.hasPermission(permission) ?: false
    }

    override fun hasPermission(perm: Permission): Boolean {
        if (perm.name == "bukkit.broadcast.user") {
            return true
        }
        return player.getCachedPermissionPlayer()?.hasPermission(perm.name) ?: false
    }

    override fun isOp(): Boolean {
        return player.getCachedPermissionPlayer()?.hasPermission("*") ?: false
    }

    override fun isPermissionSet(name: String): Boolean {
        return player.getCachedPermissionPlayer()?.hasPermission(name) ?: false
    }

    override fun isPermissionSet(perm: Permission): Boolean {
        return player.getCachedPermissionPlayer()?.hasPermission(perm.name) ?: false
    }

    override fun recalculatePermissions() {

    }

    override fun clearPermissions() {
        player.getCachedPermissionPlayer()?.clearPermissions() ?: return
    }

}