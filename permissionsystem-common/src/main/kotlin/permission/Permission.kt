package permission

class Permission(
    val permissionName: String,
    val timeout: Long
) {

    /**
     * @return true if the permission is still valid, false otherwise
     */
    fun isExpired(): Boolean {
        return System.currentTimeMillis() > timeout
    }

    /**
     * @return the remaining time in milliseconds from the time the permission was granted
     */
    fun getRemainingTime(): Long {
        return timeout - System.currentTimeMillis()
    }
}