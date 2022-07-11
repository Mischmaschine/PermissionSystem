package de.permissionsystem.velocity.permission

import com.velocitypowered.api.permission.PermissionFunction
import com.velocitypowered.api.permission.PermissionProvider
import com.velocitypowered.api.permission.PermissionSubject
import com.velocitypowered.api.proxy.Player
import permission.player.manager.PermissionPlayerManager

class PermissionProviderSurrogate(private val permissionPlayerManager: PermissionPlayerManager) : PermissionProvider {

    override fun createFunction(subject: PermissionSubject?): PermissionFunction {
        if (subject !is Player) return PermissionFunction.ALWAYS_TRUE
        return PermissionFunctionSurrogate(subject, permissionPlayerManager)
    }
}