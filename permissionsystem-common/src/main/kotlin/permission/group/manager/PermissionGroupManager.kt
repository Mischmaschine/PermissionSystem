package permission.group.manager

import permission.data.groupdata.IPermissionGroupData
import permission.future.FutureAction
import permission.group.PermissionGroup

internal class PermissionGroupManager(private val permissionGroupData: IPermissionGroupData) {

    private val permissionGroups = mutableMapOf<String, PermissionGroup>()

    fun getPermissionGroup(name: String): FutureAction<PermissionGroup> {
        return getCachedPermissionGroup(name)?.let {
            FutureAction(it)
        } ?: permissionGroupData.getPermissionGroupData(name)
    }

    fun deletePermissionGroup(permissionGroupName: String) {
        this.permissionGroups.remove(permissionGroupName)
        permissionGroupData.deletePermissionGroupData(permissionGroupName)
    }

    fun createPermissionGroup(permissionGroup: PermissionGroup) {
        updatePermissionGroupCache(permissionGroup)
        permissionGroupData.setPermissionGroupData(permissionGroup)
    }

    fun updatePermissionGroup(permissionGroup: PermissionGroup) {
        updatePermissionGroupCache(permissionGroup)
        permissionGroupData.updatePermissionGroupData(permissionGroup)
    }

    fun updatePermissionGroupCache(permissionGroup: PermissionGroup) {
        this.permissionGroups[permissionGroup.getName()] = permissionGroup
    }

    fun getCachedPermissionGroup(name: String): PermissionGroup? {
        return permissionGroups[name]
    }

    fun getAllCachedPermissionGroups(): List<PermissionGroup> {
        return permissionGroups.values.toList()
    }

    companion object {
        lateinit var instance: PermissionGroupManager
    }
}