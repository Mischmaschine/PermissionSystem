package permission

import kotlinx.serialization.Serializable

@Serializable
class Permission(
    val permissionName: String = "",
    val timeout: Long = 0
) {

    /**
     * @return true if the permission is still valid, false otherwise
     */
    fun isExpired(): Boolean = System.currentTimeMillis() > timeout

    fun isNotExpired(): Boolean = !isExpired()

    /**
     * @return the remaining time in milliseconds from the time the permission was granted
     */
    fun getRemainingTime(): Long = timeout - System.currentTimeMillis()
}