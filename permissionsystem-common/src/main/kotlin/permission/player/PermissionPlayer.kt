package permission.player

import kotlinx.serialization.Serializable
import permission.Permission
import permission.group.PermissionGroup
import permission.group.PermissionInfoGroup
import permission.group.manager.PermissionGroupManager
import permission.serialization.CollectionSerializer
import permission.serialization.UUIDSerializer
import java.util.*

/**
 * This class represents a permission player. It is used to store the permissions of a player.
 * @param uuid The uuid of the player.
 * @param permissions The permissions of the player.
 * @param groups The groups of the player.
 */
@Serializable
class PermissionPlayer(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID,
    @Serializable(with = CollectionSerializer::class)
    private val permissions: MutableCollection<Permission> = mutableSetOf(),
    @Serializable(with = CollectionSerializer::class)
    private val groups: MutableCollection<PermissionInfoGroup> = mutableSetOf(),
) : PermissionEntity {

    override fun getPermissions(): MutableCollection<Permission> {
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

    fun removePermissionInfoGroup(groupName: String) {
        val group = groups.firstOrNull { it.groupName == groupName } ?: return
        groups.remove(group)
    }

    fun getAllNotExpiredPermissionInfoGroups(): Collection<PermissionInfoGroup> {
        return getPermissionGroups().filter { !it.isExpired() }
    }

    fun getAllNotExpiredPermissionGroups(): Collection<PermissionGroup> {
        val permissionInfoGroups = getAllNotExpiredPermissionInfoGroups().mapNotNull {
            val infoGroup = PermissionGroupManager.instance.getPermissionGroup(it.groupName).get()
            infoGroup?.let { permissionGroup ->
                removePermissionInfoGroup(permissionGroup.getName())
            }
            infoGroup
        }
        return permissionInfoGroups
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