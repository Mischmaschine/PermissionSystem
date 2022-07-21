package de.permission.player

import de.permission.future.FutureAction
import de.permission.group.PermissionGroup
import de.permission.group.PermissionInfoGroup
import de.permission.group.manager.PermissionGroupManager
import de.permission.serialization.CollectionSerializer
import de.permission.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
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
    private val permissions: MutableCollection<de.permission.Permission> = mutableListOf(),
    @Serializable(with = CollectionSerializer::class)
    private val groups: MutableCollection<PermissionInfoGroup> = mutableListOf(),
) : PermissionEntity {

    override fun getPermissions(): MutableCollection<de.permission.Permission> {
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
        if (groups.isEmpty()) return emptyList()
        return getPermissionGroups().filter { !it.isExpired() }
    }

    fun getAllNotExpiredPermissionGroups(): Collection<PermissionGroup> {
        val permissionInfoGroups: List<FutureAction<PermissionGroup>> =
            getAllNotExpiredPermissionInfoGroups().map {
                val permissionGroup = PermissionGroupManager.instance.getPermissionGroup(it.groupName)
                permissionGroup.onFailure { _ ->
                    removePermissionInfoGroup(it.groupName)
                }
                permissionGroup
            }
        val permissionGroups = mutableListOf<PermissionGroup>()

        permissionInfoGroups.forEach {
            it.onSuccess { permissionGroup ->
                permissionGroups.add(permissionGroup)
            }
        }
        return permissionGroups
    }

    override fun addPermission(permission: de.permission.Permission) {
        this.permissions.add(permission)
    }

    override fun hasPermission(permission: String): Boolean {
        val permissionName = getAllNotExpiredPermissions().find { it.name == permission }
        permissionName?.let { return true }

        val permissionBool = getAllNotExpiredPermissionGroups().any { it.hasPermission(permission) }
        return if (permissionBool) {
            true
        } else {
            hasAllPermissions()
        }
    }

    override fun removePermission(permission: de.permission.Permission) {
        this.permissions.removeIf { it == permission }
    }
}