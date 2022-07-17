package permission.data.sql.group

import kotlinx.serialization.decodeFromString
import permission.data.groupdata.IPermissionGroupData
import permission.data.sql.MySQL
import permission.future.FutureAction
import permission.group.PermissionGroup

class PermissionGroupDataMySQL(private val mySQL: MySQL) : IPermissionGroupData {

    override fun getPermissionGroupData(name: String): FutureAction<PermissionGroup> {
        val futureAction = FutureAction<PermissionGroup>()
        executors.submit {
            val resultSet = mySQL.getResultSync(PERMISSION_GROUP_TABLE, "group", name)?.getResultSet()
            resultSet?.let {
                if (it.next()) {
                    futureAction.complete(json.decodeFromString<PermissionGroup>(it.getString(PERMISSION_GROUP_DATA)))
                } else {
                    futureAction.completeExceptionally(IllegalArgumentException("Permission group $name not found"))
                }
            }
        }
        return futureAction
    }

    override fun updatePermissionGroupData(permissionGroup: PermissionGroup) {
        mySQL.updateAsync(
            PERMISSION_GROUP_TABLE,
            "name",
            permissionGroup.getName(),
            "group",
            permissionGroup.encodeToString()
        )
    }

    override fun setPermissionGroupData(permissionGroup: PermissionGroup) {
        mySQL.insertAsync(PERMISSION_GROUP_TABLE, listOf(permissionGroup.getName(), permissionGroup.encodeToString()))
    }

    override fun deletePermissionGroupData(permissionGroupName: String) {
        mySQL.deleteAsync(PERMISSION_GROUP_TABLE, "group", permissionGroupName)
    }

    companion object {
        const val PERMISSION_GROUP_TABLE = "permission_group"
        const val PERMISSION_GROUP_DATA = "permission_group_data"
    }
}