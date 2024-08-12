package dev.yuanzix.beatify.models

import androidx.datastore.core.Serializer
import dev.yuanzix.beatify.utils.CryptoManager
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@Serializable
data class UserSettings(
    val email: String? = null,
    val jwtTokenString: String? = null,
)

class UserSettingsSerializer @Inject constructor(
    private val cryptoManager: CryptoManager
) : Serializer<UserSettings> {
    override val defaultValue: UserSettings
        get() = UserSettings()

    override suspend fun readFrom(input: InputStream): UserSettings {
        val decryptedBytes = cryptoManager.decrypt(input)
        return try {
            Json.decodeFromString(UserSettings.serializer(), decryptedBytes.decodeToString())
        } catch (e: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        cryptoManager.encrypt(
            Json.encodeToString(UserSettings.serializer(), t).encodeToByteArray(),
            output
        )
    }

}
