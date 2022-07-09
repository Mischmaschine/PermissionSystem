package permission.group

class PermissionInfoGroup(val groupName: String, val timeout: Long) {

    fun isExpired(): Boolean = System.currentTimeMillis() > timeout

}