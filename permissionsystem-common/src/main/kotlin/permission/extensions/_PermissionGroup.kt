package permission.extensions

import permission.group.PermissionGroup
import permission.group.manager.PermissionGroupManager

fun PermissionGroup.update() {
    PermissionGroupManager.instance.updatePermissionGroup(this)
}