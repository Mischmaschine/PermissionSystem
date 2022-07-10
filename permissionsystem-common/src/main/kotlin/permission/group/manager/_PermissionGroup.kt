package permission.group.manager

import permission.group.PermissionGroup

fun PermissionGroup.update() {
    PermissionGroupManager.instance.updatePermissionGroup(this)
}

fun PermissionGroup.delete() {
    PermissionGroupManager.instance.deletePermissionGroup(this)
}