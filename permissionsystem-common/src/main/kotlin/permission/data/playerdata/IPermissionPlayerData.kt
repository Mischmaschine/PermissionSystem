package permission.data.playerdata

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import permission.player.PermissionPlayer
import java.util.*
import java.util.concurrent.CompletableFuture

interface IPermissionPlayerData {

    /**
     * Gets the [PermissionPlayer] associated with this data.
     */
    fun getPermissionPlayerData(uuid: UUID): CompletableFuture<PermissionPlayer?>

    /**
     * Updates the [PermissionPlayer] associated with this data.
     */
    fun updatePermissionPlayerData(permissionPlayer: PermissionPlayer)

    /**
     * Sets the [PermissionPlayer] associated with this data.
     */
    fun setPermissionPlayerData(permissionPlayer: PermissionPlayer)

    val gson: Gson
        get() = GsonBuilder().setPrettyPrinting().create()
}