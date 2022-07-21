package de.permission.group.manager

import de.permission.data.groupdata.IPermissionGroupData
import de.permission.future.FutureAction
import de.permission.group.PermissionGroup

class PermissionGroupManager(private val permissionGroupData: IPermissionGroupData) {

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