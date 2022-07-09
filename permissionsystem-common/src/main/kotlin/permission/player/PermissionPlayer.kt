package permission.player

import permission.Permission
import permission.group.PermissionGroup
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
    val groups: List<PermissionGroup>,
) : PermissionEntity {

    override fun getPermissions(): MutableSet<Permission> {
        val permissions = this.permissions
        groups.forEach {
            permissions.addAll(it.getPermissions())
        }
        return permissions
    }

    override fun addPermission(permission: Permission) {
        this.permissions.add(permission)
    }

    override fun removePermission(permission: Permission) {
        this.permissions.removeIf { it == permission }
    }
}