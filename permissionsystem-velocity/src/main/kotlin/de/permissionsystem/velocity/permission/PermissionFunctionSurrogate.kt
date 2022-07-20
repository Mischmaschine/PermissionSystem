package de.permissionsystem.velocity.permission

import com.velocitypowered.api.permission.PermissionFunction
import com.velocitypowered.api.permission.Tristate
import com.velocitypowered.api.proxy.Player
import de.permissionsystem.velocity.extenstions.getCachedPermissionPlayer

class PermissionFunctionSurrogate(
    private val player: Player,
) : PermissionFunction {

    override fun getPermissionValue(permission: String?): Tristate {
        if (permission == null) {
            return Tristate.FALSE
        }
        val permissionPlayer = player.getCachedPermissionPlayer() ?: return Tristate.FALSE
        return Tristate.fromBoolean(permissionPlayer.hasPermission(permission))
    }
}