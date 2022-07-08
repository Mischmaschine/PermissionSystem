import net.md_5.bungee.api.connection.ProxiedPlayer
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager
import java.util.concurrent.CompletableFuture

fun ProxiedPlayer.getPermissionPlayer(permissionPlayerManager: PermissionPlayerManager): CompletableFuture<PermissionPlayer?> {
    return permissionPlayerManager.getPermissionPlayer(this.uniqueId)
}