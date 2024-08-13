package dev.yuanzix.beatify.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import dev.yuanzix.beatify.models.UserSettings
import dev.yuanzix.beatify.models.UserSettingsSerializer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

val Context.userSettingsDataStore: DataStore<UserSettings> by dataStore(
    fileName = "user_settings.pb",
    serializer = UserSettingsSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler { UserSettings() }
)

class DataStoreRepository @Inject constructor(
    context: Context,
) {

    private val dataStore = context.userSettingsDataStore

    val userSettings: Flow<UserSettings> = dataStore.data

    suspend fun updateUserSettings(email: String?, jwtTokenString: String?) {
        dataStore.updateData { currentSettings ->
            currentSettings.copy(
                email = email ?: currentSettings.email,
                jwtTokenString = jwtTokenString ?: currentSettings.jwtTokenString
            )
        }
    }

    suspend fun clearUserSettings() {
        dataStore.updateData {
            UserSettings()
        }
    }
}

