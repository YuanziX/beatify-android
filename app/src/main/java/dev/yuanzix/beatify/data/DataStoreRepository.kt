package dev.yuanzix.beatify.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.yuanzix.beatify.models.UserSettings
import dev.yuanzix.beatify.models.UserSettingsSerializer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreRepository
@Inject constructor(
    @ApplicationContext context: Context, serializer: UserSettingsSerializer,
) {
    private val Context.userSettingsDataStore: DataStore<UserSettings> by dataStore(
        fileName = "user_settings.json", serializer = serializer
    )

    private val dataStore = context.userSettingsDataStore

    val userSettingsFlow: Flow<UserSettings> = dataStore.data

    suspend fun saveUserSettings(userSettings: UserSettings) {
        dataStore.updateData {
            userSettings
        }
    }

    suspend fun clearUserSettings() {
        dataStore.updateData {
            UserSettings()
        }
    }
}
