package permission.group

import permission.Permission

class PermissionGroup(
    private val name: String,
    private val permissions: MutableSet<Permission>,
    private val inheritances: MutableSet<PermissionGroup>,
    private val priority: Int
) : IPermissionGroup {

    override fun addInheritance(group: PermissionGroup) {
        inheritances.add(group)
    }

    override fun removeInheritance(group: PermissionGroup) {
        inheritances.remove(group)
    }

    override fun getAllInheritances(): Collection<PermissionGroup> {
        return inheritances
    }

    override fun getPermissions(): MutableCollection<Permission> {
        val permissions = this.permissions
        getAllInheritances().forEach {
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

    override fun getPriority(): Int = priority
}
