import org.bukkit.entity.Player
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager
import java.util.concurrent.CompletableFuture

fun Player.getPermissionPlayer(permissionPlayerManager: PermissionPlayerManager): CompletableFuture<PermissionPlayer?> {
    return permissionPlayerManager.getPermissionPlayer(this.uniqueId)
}