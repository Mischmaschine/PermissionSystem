package permission.data.mongodb.group

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.bson.Document
import permission.data.groupdata.IPermissionGroupData
import permission.data.mongodb.MongoDB
import permission.future.FutureAction
import permission.group.PermissionGroup

internal class PermissionGroupDataMongoDB(private val mongoDB: MongoDB) : IPermissionGroupData {

    override fun getPermissionGroupData(name: String): FutureAction<PermissionGroup> {
        val future = FutureAction<PermissionGroup>()

        executors.submit {
            val permissionGroup = mongoDB.getDocumentSync(PERMISSION_GROUP_COLLECTION, name)
                ?.getString(PERMISSION_GROUP_DATA)?.let {
                    json.decodeFromString<PermissionGroup>(
                        it
                    )
                }
            permissionGroup?.let {
                future.complete(it)
            } ?: future.completeExceptionally(NullPointerException("permissionGroup is null"))
        }

        return future
    }

    override fun updatePermissionGroupData(permissionGroup: PermissionGroup) {
        mongoDB.updateDocumentAsync(
            PERMISSION_GROUP_COLLECTION,
            permissionGroup.getName(),
            Document(PERMISSION_GROUP_DATA, json.encodeToString(permissionGroup))
        )
    }

    override fun setPermissionGroupData(permissionGroup: PermissionGroup) {
        mongoDB.insertDocumentAsync(
            PERMISSION_GROUP_COLLECTION,
            permissionGroup.getName(),
            Document(PERMISSION_GROUP_DATA, json.encodeToString(permissionGroup))
        )
    }

    override fun deletePermissionGroupData(permissionGroupName: String) {
        mongoDB.deleteDocumentAsync(PERMISSION_GROUP_COLLECTION, permissionGroupName)
    }

    companion object {
        const val PERMISSION_GROUP_COLLECTION = "permissionGroup"
        const val PERMISSION_GROUP_DATA = "permissionGroupData"
    }
}