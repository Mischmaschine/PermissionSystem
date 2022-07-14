package de.permission

import de.permission.extensions.getPermissionPlayer
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissibleBase
import org.bukkit.permissions.Permission

internal class PermissibleBaseSurrogate(private val player: Player) : PermissibleBase(player) {

    override fun hasPermission(permission: String): Boolean {
        if (permission == "bukkit.broadcast.user") {
            return true
        }
        var hasPermission = false
        player.getPermissionPlayer().onSuccess {
            hasPermission = it.hasPermission(permission)
        }
        return hasPermission
    }

    override fun hasPermission(perm: Permission): Boolean {
        if (perm.name == "bukkit.broadcast.user") {
            return true
        }
        var hasPermission = false
        player.getPermissionPlayer().onSuccess {
            hasPermission = it.hasPermission(perm.name)
        }
        return hasPermission
    }

    override fun isOp(): Boolean {
        var hasPermission = false
        player.getPermissionPlayer().onSuccess {
            hasPermission = it.hasAllPermissions()
        }
        return hasPermission
    }

    override fun isPermissionSet(name: String): Boolean {
        var hasPermission = false
        player.getPermissionPlayer().onSuccess {
            hasPermission = it.hasPermission(name)
        }
        return hasPermission
    }

    override fun isPermissionSet(perm: Permission): Boolean {
        var hasPermission = false
        player.getPermissionPlayer().onSuccess {
            hasPermission = it.hasPermission(perm.name)
        }
        return hasPermission
    }

    override fun recalculatePermissions() {

    }

/*    override fun clearPermissions() {
        super.clearPermissions()
        player.getCachedPermissionPlayer()?.clearPermissions() ?: return
    }*/
}