package de.permission.player

import de.permission.Permission

internal interface PermissionEntity {

    /**
     * Returns all permissions of this entity.
     */
    fun getPermissions(): MutableCollection<Permission>

    /**
     * Returns the permission from the given name.
     */
    fun getPermissionByName(name: String): Permission? =
        getAllNotExpiredPermissions().find { it.name == name }

    /**
     * Returns all permissions of this entity which are not expired.
     */
    fun getAllNotExpiredPermissions(): Collection<Permission> =
        getPermissions().filter {
            it.isExpired().let { boolean ->
                if (boolean) {
                    removePermission(it)
                }
                boolean
            }
        }

    /**
     * Sets the permission to the given value.
     */
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
    fun hasPermission(permission: String): Boolean =
        getPermissionByName(permission)?.isNotExpired() ?: hasAllPermissions()

    /**
     * Returns true if the entity has the * permission.
     */
    fun hasAllPermissions(): Boolean = getPermissions().any { it.name == "*" }

    /**
     * Clears all permissions from this entity.
     */
    fun clearPermissions() = getPermissions().clear()
}