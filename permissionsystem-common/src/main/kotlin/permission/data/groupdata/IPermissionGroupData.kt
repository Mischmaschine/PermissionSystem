package permission.data.groupdata

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import permission.group.PermissionGroup
import permission.player.PermissionPlayer
import java.util.concurrent.CompletableFuture

interface IPermissionGroupData {

    /**
     * Gets the [PermissionPlayer] associated with this data.
     */
    fun getPermissionGroupData(name: String): CompletableFuture<PermissionGroup?>

    /**
     * Updates the [PermissionPlayer] associated with this data.
     */
    fun updatePermissionGroupData(permissionGroup: PermissionGroup)

    /**
     * Sets the [PermissionPlayer] associated with this data.
     */
    fun setPermissionGroupData(permissionGroup: PermissionGroup)

    /**
     * Removes the [PermissionPlayer] associated with this data.
     */
    fun removePermissionGroupData(permissionGroup: PermissionGroup)

    val gson: Gson
        get() = GsonBuilder().setPrettyPrinting().create()

}