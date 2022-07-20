import permission.future.FutureAction
import permission.player.PermissionPlayer
import java.util.*

fun main() {

    FutureAction {
        this.complete(PermissionPlayer(UUID.randomUUID()))
    }.whenComplete { result, _ ->
        println(result.uuid)
        println(result.getAllNotExpiredPermissionGroups())
        println(result.getPermissionGroups())
    }
}