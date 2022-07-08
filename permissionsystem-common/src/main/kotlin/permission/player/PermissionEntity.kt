package permission.player

import permission.Permission

interface PermissionEntity {

    /**
     * Returns all permissions of this entity.
     */
    fun getPermissions(): Set<Permission>

    /**
     * Returns the permission from the given name.
     */
    fun getPermissionByName(name: String): Permission? {
        return getPermissions().firstOrNull { it.permissionName == name }
    }

    fun setPermission(permission: Permission) {
        clearPermissions()
        addPermission(permission)
    }

    /**
     * Adds the given permission to this entity.
     */
    fun addPermission(permission: Permission)

    /**
     * Removes the given permission from this entity.
     */
    fun removePermission(permission: Permission)

    /**
     * Removes the given permission from this entity.
     */
    fun removePermission(permissionName: String) {
        removePermission(getPermissionByName(permissionName) ?: return)
    }

    /**
     * Returns true if the given permission is granted to this entity.
     */
    fun hasPermission(permission: String): Boolean {
        return getPermissionByName(permission) != null
    }

    /**
     * Clears all permissions from this entity.
     */
    fun clearPermissions() {
        getPermissions().forEach { removePermission(it) }
    }

}