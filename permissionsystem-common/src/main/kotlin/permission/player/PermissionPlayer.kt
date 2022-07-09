package permission.player

import permission.Permission
import permission.group.PermissionGroup
import permission.group.PermissionInfoGroup
import permission.group.manager.PermissionGroupManager
import java.util.*

/**
 * This class represents a permission player. It is used to store the permissions of a player.
 * @param uuid The uuid of the player.
 * @param permissions The permissions of the player.
 * @param groups The groups of the player.
 */
class PermissionPlayer(
    val uuid: UUID,
    private val permissions: MutableSet<Permission>,
    val groups: MutableCollection<PermissionInfoGroup>,
) : PermissionEntity {

    override fun getPermissions(): MutableSet<Permission> {
        val permissions = this.permissions
        getAllNotExpiredPermissionGroups().map { it.getPermissions() }.forEach { permissions.addAll(it) }
        return this.permissions
    }

    fun getPermissionGroups(): Collection<PermissionInfoGroup> {
        return groups
    }

    fun addPermissionInfoGroup(group: PermissionInfoGroup) {
        this.groups.add(group)
    }

    fun getAllNotExpiredPermissionInfoGroups(): Collection<PermissionInfoGroup> {
        return getPermissionGroups().filter { !it.isExpired() }
    }

    fun getAllNotExpiredPermissionGroups(): Collection<PermissionGroup> =
        getAllNotExpiredPermissionInfoGroups().mapNotNull {
            PermissionGroupManager.instance.getPermissionGroup(it.groupName).get()
        }

    override fun addPermission(permission: Permission) {
        this.permissions.add(permission)
    }

    override fun hasPermission(permission: String): Boolean {
        val permissionName = getAllNotExpiredPermissions().find { it.permissionName == permission }
        permissionName?.let { return it.isExpired() }

        val permissionBool = getAllNotExpiredPermissionGroups().any { it.hasPermission(permission) }
        return if (permissionBool) {
            true
        } else {
            hasAllPermissions()
        }
    }

    override fun removePermission(permission: Permission) {
        this.permissions.removeIf { it == permission }
    }
}