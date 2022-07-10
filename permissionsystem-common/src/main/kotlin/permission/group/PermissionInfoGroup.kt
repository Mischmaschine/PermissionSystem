package permission.group

import kotlinx.serialization.Serializable

@Serializable
data class PermissionInfoGroup(val groupName: String, val timeout: Long) {

    fun isExpired(): Boolean = System.currentTimeMillis() > timeout

}