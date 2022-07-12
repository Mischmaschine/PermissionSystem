package permission.data.groupdata

import kotlinx.serialization.json.Json
import permission.future.FutureAction
import permission.group.PermissionGroup
import permission.player.PermissionPlayer

interface IPermissionGroupData {

    /**
     * Gets the [PermissionPlayer] associated with this data.
     */
    fun getPermissionGroupData(name: String): FutureAction<PermissionGroup?>

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