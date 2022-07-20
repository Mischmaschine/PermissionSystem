package de.permissionsystem.velocity.permission

import com.velocitypowered.api.permission.PermissionFunction
import com.velocitypowered.api.permission.PermissionProvider
import com.velocitypowered.api.permission.PermissionSubject
import com.velocitypowered.api.proxy.Player

class PermissionProviderSurrogate : PermissionProvider {

    override fun createFunction(subject: PermissionSubject?): PermissionFunction {
        if (subject !is Player) return PermissionFunction.ALWAYS_TRUE
        return PermissionFunctionSurrogate(subject)
    }
}