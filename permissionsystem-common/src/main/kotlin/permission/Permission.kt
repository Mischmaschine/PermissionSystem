package permission

class Permission(val permissionName: String, val permissionDescription: String, val timeout: Long) {

    fun isExpired(): Boolean {
        return System.currentTimeMillis() > timeout
    }

    fun getRemainingTime(): Long {
        return timeout - System.currentTimeMillis()
    }
}