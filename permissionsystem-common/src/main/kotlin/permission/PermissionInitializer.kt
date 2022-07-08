package permission

import permission.data.sql.MySQL
import permission.data.sql.PermissionPlayerMySQL
import permission.player.manager.PermissionPlayerManager

class PermissionInitializer {

    var permissionPlayerManager: PermissionPlayerManager = PermissionPlayerManager(PermissionPlayerMySQL(MySQL())) //TODO make this configurable

}