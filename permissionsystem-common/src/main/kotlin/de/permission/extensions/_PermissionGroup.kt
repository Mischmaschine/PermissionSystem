package de.permission.extensions

import de.permission.group.PermissionGroup
import de.permission.group.manager.PermissionGroupManager

fun PermissionGroup.update() {
    PermissionGroupManager.instance.updatePermissionGroup(this)
}