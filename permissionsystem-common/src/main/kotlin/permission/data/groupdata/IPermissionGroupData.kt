package permission.data.groupdata

import kotlinx.serialization.json.Json
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
    fun deletePermissionGroupData(permissionGroup: PermissionGroup)

    /**
     * Returns Json serializer.
     */
    val json: Json
        get() = Json { prettyPrint = true }
}