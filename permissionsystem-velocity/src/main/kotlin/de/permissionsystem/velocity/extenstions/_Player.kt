package de.permissionsystem.velocity.extenstions

import com.velocitypowered.api.proxy.Player
import de.permission.future.FutureAction
import de.permission.player.PermissionPlayer
import de.permissionsystem.velocity.velocityplugin.VelocityPluginMain

fun Player.getPermissionPlayer(): FutureAction<PermissionPlayer> {
    return VelocityPluginMain.instance.permissionInitializer.permissionPlayerManager.getPermissionPlayer(this.uniqueId)
}

fun Player.getCachedPermissionPlayer(): PermissionPlayer? {
    return VelocityPluginMain.instance.permissionInitializer.permissionPlayerManager.getCachedPermissionPlayer(this.uniqueId)
}