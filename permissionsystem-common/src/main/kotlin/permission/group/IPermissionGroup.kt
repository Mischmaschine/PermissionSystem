package permission.group

import permission.player.PermissionEntity

interface IPermissionGroup : PermissionEntity {

    /**
     * Adds an inherited permission group.
     */
    fun addInheritance(group: PermissionGroup)

    /**
     * Removes an inherited permission group.
     */
    fun removeInheritance(group: PermissionGroup)

    /**
     * Returns all inherited permission groups.
     */
    fun getAllInheritances(): Collection<PermissionGroup>

    /**
     * Returns the priority of this group.
     */
    fun getPriority(): Int

    /**
     * Returns true if the group has the specified permission.
     */
    override fun hasPermission(permission: String): Boolean {
        return super.hasPermission(permission) || getAllInheritances().any { it.hasPermission(permission) }
    }
}