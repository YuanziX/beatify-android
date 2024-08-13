package dev.yuanzix.beatify.models

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class UserSettings(
    val email: String? = null,
    val jwtTokenString: String? = null,
)


object UserSettingsSerializer : Serializer<UserSettings> {
    override val defaultValue: UserSettings = UserSettings()

    override suspend fun readFrom(input: InputStream): UserSettings {
        return try {
            Json.decodeFromString(UserSettings.serializer(), input.readBytes().decodeToString())
        } catch (exception: SerializationException) {
            throw CorruptionException("Unable to read UserSettings", exception)
        }
    }

    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(UserSettings.serializer(), t).encodeToByteArray())
        }
    }
}