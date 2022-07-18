package permission.serialization

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import permission.data.DatabaseConfiguration

interface DefaultJsonConfiguration {

    val json: Json
        get() = Json {
            prettyPrint = true
            encodeDefaults = true
            SerializersModule {
                polymorphic(DatabaseConfiguration::class) {
                    subclass(DatabaseConfiguration::class, DatabaseConfiguration.serializer())
                }
            }
        }

    fun encodeToString(): String = json.encodeToString(this)

}