package permission

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
class Permission(
    @JsonNames("permissionName")
    val name: String = "",
    val timeout: Long = 0
) {

    /**
     * @return true if the permission is expired and should be removed
     */
    fun isExpired(): Boolean = System.currentTimeMillis() > timeout

    /**
     * @return true if the permission is still valid, false otherwise
     */
    fun isNotExpired(): Boolean = !isExpired()

    /**
     * @return the remaining time in milliseconds from the time the permission was granted
     */
    fun getRemainingTime(): Long = timeout - System.currentTimeMillis()
}