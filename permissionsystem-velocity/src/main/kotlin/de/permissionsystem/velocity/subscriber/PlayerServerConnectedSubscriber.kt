package de.permissionsystem.velocity.subscriber

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerConnectedEvent
import permission.extensions.update
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager

class PlayerServerConnectedSubscriber(private val permissionPlayerManager: PermissionPlayerManager) {

    @Subscribe
    fun onServerConnected(event: ServerConnectedEvent) {
        permissionPlayerManager.getPermissionPlayer(event.player.uniqueId).onSuccess {
            it.getPermissions().filter { permission -> permission.isExpired() }.forEach(it::removePermission)
                .also { _ ->
                    it.update()
                }
        }.onFailure {
            permissionPlayerManager.createPermissionPlayer(PermissionPlayer(event.player.uniqueId))
        }
    }

}