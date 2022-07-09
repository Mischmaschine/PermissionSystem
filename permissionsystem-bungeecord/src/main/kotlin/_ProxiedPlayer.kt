import net.md_5.bungee.api.connection.PendingConnection
import net.md_5.bungee.api.connection.ProxiedPlayer
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager
import java.util.concurrent.CompletableFuture

@Suppress("DEPRECATION")
fun ProxiedPlayer.getPermissionPlayer(): CompletableFuture<PermissionPlayer?> {
    return PermissionPlayerManager.instance.getPermissionPlayer(this.uniqueId)
}

@Suppress("DEPRECATION")
fun PendingConnection.getPermissionPlayer(): CompletableFuture<PermissionPlayer?> {
    return PermissionPlayerManager.instance.getPermissionPlayer(uniqueId)
}

@Suppress("DEPRECATION")
fun ProxiedPlayer.getCachedPermissionPlayer(): PermissionPlayer? {
    return PermissionPlayerManager.instance.getCachedPermissionPlayer(this.uniqueId)
}