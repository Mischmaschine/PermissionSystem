package de.permissionsystem.velocity.extenstions

import com.velocitypowered.api.proxy.Player
import de.permissionsystem.velocity.velocityplugin.VelocityPluginMain
import permission.future.FutureAction
import permission.player.PermissionPlayer

fun Player.getPermissionPlayer(): FutureAction<PermissionPlayer> {
    return VelocityPluginMain.instance.permissionInitializer.permissionPlayerManager.getPermissionPlayer(this.uniqueId)
}

fun Player.getCachedPermissionPlayer(): PermissionPlayer? {
    return VelocityPluginMain.instance.permissionInitializer.permissionPlayerManager.getCachedPermissionPlayer(this.uniqueId)
}