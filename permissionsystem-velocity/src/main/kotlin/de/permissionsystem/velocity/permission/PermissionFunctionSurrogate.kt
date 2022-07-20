package de.permissionsystem.velocity.permission

import com.velocitypowered.api.permission.PermissionFunction
import com.velocitypowered.api.permission.Tristate
import com.velocitypowered.api.proxy.Player
import permission.player.manager.PermissionPlayerManager

class PermissionFunctionSurrogate(
    private val player: Player,
    private val permissionPlayerManager: PermissionPlayerManager
) : PermissionFunction {

    override fun getPermissionValue(permission: String): Tristate {
        val permissionPlayer =
            permissionPlayerManager.getCachedPermissionPlayer(player.uniqueId) ?: return Tristate.FALSE
        return Tristate.fromBoolean(permissionPlayer.hasPermission(permission))
    }
}