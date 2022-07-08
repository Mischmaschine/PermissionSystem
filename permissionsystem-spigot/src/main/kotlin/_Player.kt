import org.bukkit.entity.Player
import permission.player.PermissionPlayer
import permission.player.manager.PermissionPlayerManager
import java.util.concurrent.CompletableFuture

fun Player.getPermissionPlayer(): CompletableFuture<PermissionPlayer?> {
    return PermissionPlayerManager.instance.getPermissionPlayer(this.uniqueId)
}