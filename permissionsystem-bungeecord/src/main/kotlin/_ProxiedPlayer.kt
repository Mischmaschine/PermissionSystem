import net.md_5.bungee.api.connection.ProxiedPlayer
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager
import java.util.concurrent.CompletableFuture

fun ProxiedPlayer.getPermissionPlayer(): CompletableFuture<PermissionPlayer?> {
    return PermissionPlayerManager.instance.getPermissionPlayer(this.uniqueId)
}

fun ProxiedPlayer.getCachedPermissionPlayer(): PermissionPlayer? {
    return PermissionPlayerManager.instance.getCachedPermissionPlayer(this.uniqueId)
}