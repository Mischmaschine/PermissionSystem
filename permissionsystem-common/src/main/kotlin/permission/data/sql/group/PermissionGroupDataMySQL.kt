package permission.data.sql.group

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import permission.data.groupdata.IPermissionGroupData
import permission.data.sql.MySQL
import permission.future.FutureAction
import permission.group.PermissionGroup

class PermissionGroupDataMySQL(private val mySQL: MySQL) : IPermissionGroupData {

    override fun getPermissionGroupData(name: String): FutureAction<PermissionGroup> {
        val futureAction = FutureAction<PermissionGroup>()
        executors.submit {
            val resultSet = mySQL.getResultSync(PERMISSION_GROUP_TABLE, "group", name).getResultSet()
            val permissionGroup: PermissionGroup? = resultSet.let {
                if (it.next()) {
                    json.decodeFromString<PermissionGroup>(it.getString(PERMISSION_GROUP_DATA))
                } else {
                    null
                }
            }
            permissionGroup?.let {
                futureAction.complete(it)
            } ?: futureAction.completeExceptionally(IllegalArgumentException("Permission group $name not found"))
        }
        return futureAction
    }

    override fun updatePermissionGroupData(permissionGroup: PermissionGroup) {
        mySQL.updateAsync(
            PERMISSION_GROUP_TABLE,
            permissionGroup.getName(),
            PERMISSION_GROUP_DATA,
            json.encodeToString(permissionGroup)
        )
    }

    override fun setPermissionGroupData(permissionGroup: PermissionGroup) {
        mySQL.insertAsync(
            PERMISSION_GROUP_TABLE,
            listOf(permissionGroup.getName(), json.encodeToString(permissionGroup))
        )
    }

    override fun deletePermissionGroupData(permissionGroupName: String) {
        mySQL.deleteAsync(PERMISSION_GROUP_TABLE, permissionGroupName)
    }

    companion object {
        const val PERMISSION_GROUP_TABLE = "permission_group"
        const val PERMISSION_GROUP_DATA = "permission_group_data"
    }
}