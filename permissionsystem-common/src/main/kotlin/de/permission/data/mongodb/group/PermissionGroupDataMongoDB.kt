package de.permission.data.mongodb.group

import de.permission.data.groupdata.IPermissionGroupData
import de.permission.data.mongodb.MongoDB
import de.permission.future.FutureAction
import de.permission.group.PermissionGroup
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.bson.Document

internal class PermissionGroupDataMongoDB(private val mongoDB: MongoDB) : IPermissionGroupData {

    override fun getPermissionGroupData(name: String): FutureAction<PermissionGroup> {
        return FutureAction {
            executors.submit {
                val permissionGroup = mongoDB.getDocumentSync(PERMISSION_GROUP_COLLECTION, name)
                    ?.getString(PERMISSION_GROUP_DATA)?.let {
                        json.decodeFromString<PermissionGroup>(
                            it
                        )
                    }
                permissionGroup?.let {
                    this.complete(it)
                } ?: this.completeExceptionally(NullPointerException("permissionGroup is null"))
            }
        }
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