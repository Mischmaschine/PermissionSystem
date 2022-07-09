package permission.group.manager

import permission.data.groupdata.IPermissionGroupData
import permission.group.PermissionGroup
import java.util.concurrent.CompletableFuture

class PermissionGroupManager(private val permissionGroupData: IPermissionGroupData) {

    private val permissionGroups = mutableMapOf<String, PermissionGroup>()

    fun getPermissionGroup(name: String): CompletableFuture<PermissionGroup?> {
        return getCachedPermissionGroup(name)?.let {
            CompletableFuture.completedFuture(it)
        } ?: permissionGroupData.getPermissionGroupData(name).thenApply {
            it?.let {
                permissionGroups[it.getName()] = it
                it
            }
        }
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