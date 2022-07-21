package de.permission.group

import kotlinx.serialization.Serializable

@Serializable
data class PermissionInfoGroup(val groupName: String, val timeout: Long) {

    /**
     * @return if the permission group is expired or not (true if expired)
     */
    fun isExpired(): Boolean = (System.currentTimeMillis() > timeout) && timeout != -1L

}