package permission.data.mongodb.group

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.bson.Document
import permission.data.groupdata.IPermissionGroupData
import permission.data.mongodb.MongoDB
import permission.group.PermissionGroup
import java.util.concurrent.CompletableFuture

class PermissionGroupDataMongoDB(private val mongoDB: MongoDB) : IPermissionGroupData {

    override fun getPermissionGroupData(name: String): CompletableFuture<PermissionGroup?> {
        return CompletableFuture.supplyAsync {
            return@supplyAsync mongoDB.getDocumentSync(PERMISSION_GROUP_COLLECTION, name)
                ?.getString(PERMISSION_GROUP_DATA)?.let {
                    json.decodeFromString(
                        it
                    )
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

    override fun deletePermissionGroupData(permissionGroup: PermissionGroup) {
        mongoDB.deleteDocumentAsync(PERMISSION_GROUP_COLLECTION, permissionGroup.getName())
    }

    companion object {
        const val PERMISSION_GROUP_COLLECTION = "permissionGroup"
        const val PERMISSION_GROUP_DATA = "permissionGroupData"
    }
}