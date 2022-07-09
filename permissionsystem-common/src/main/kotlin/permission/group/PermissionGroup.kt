package permission.group

import permission.Permission
import permission.player.PermissionEntity

class PermissionGroup(
    private val name: String,
    private val permissions: MutableSet<Permission>,
    private val inheritances: MutableSet<PermissionGroup>,
    private val priority: Int,
    private val prefix: String,
    private val suffix: String,
    private val displayName: String = prefix + name + suffix
) : PermissionEntity {

    fun addInheritance(group: PermissionGroup) {
        inheritances.add(group)
    }

    fun removeInheritance(group: PermissionGroup) {
        inheritances.remove(group)
    }

    fun getInheritances(): Set<PermissionGroup> = inheritances

    override fun getPermissions(): MutableSet<Permission> {
        val permissions = this.permissions
        getInheritances().forEach {
            permissions.addAll(it.getPermissions())
        }
        return permissions
    }

    override fun addPermission(permission: Permission) {
        this.permissions.add(permission)
    }

    override fun removePermission(permission: Permission) {
        this.permissions.remove(permission)
    }

    fun getName(): String = name

    fun getPriority(): Int = priority

    fun getPrefix(): String = prefix

    fun getSuffix(): String = suffix

    fun getDisplayName(): String = displayName
}
