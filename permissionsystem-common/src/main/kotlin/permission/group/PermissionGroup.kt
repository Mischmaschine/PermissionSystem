package permission.group

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import permission.Permission
import permission.serialization.CollectionSerializer

@Serializable
data class PermissionGroup(
    @SerialName("name")
    private val name: String,
    @SerialName("permissions")
    @Serializable(with = CollectionSerializer::class)
    private val permissions: MutableCollection<Permission> = mutableSetOf(),
    @SerialName("inheritances")
    @Serializable(with = CollectionSerializer::class)
    private val inheritances: MutableCollection<PermissionGroup> = mutableSetOf(),
    @SerialName("priority")
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
