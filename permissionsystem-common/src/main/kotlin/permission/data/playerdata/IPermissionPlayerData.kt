package permission.data.playerdata

import kotlinx.serialization.json.Json
import permission.future.FutureAction
import permission.player.PermissionPlayer
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

interface IPermissionPlayerData {

    /**
     * Gets the [PermissionPlayer] associated with this data.
     */
    fun getPermissionPlayerData(uuid: UUID): FutureAction<PermissionPlayer?>

    /**
     * Updates the [PermissionPlayer] associated with this data.
     */
    fun updatePermissionPlayerData(permissionPlayer: PermissionPlayer)

    /**
     * Sets the [PermissionPlayer] associated with this data.
     */
    fun setPermissionPlayerData(permissionPlayer: PermissionPlayer)

    val json: Json
        get() = Json { prettyPrint = true }

    val executors: ExecutorService
        get() = Executors.newCachedThreadPool()

}