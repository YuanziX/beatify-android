package dev.yuanzix.beatify.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.yuanzix.beatify.data.loginSignupRepository.AuthRepository
import dev.yuanzix.beatify.models.UserSettingsSerializer
import dev.yuanzix.beatify.utils.CryptoManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLoginSignupRepository() = AuthRepository()

    @Provides
    @Singleton
    fun provideCryptoManager() = CryptoManager()

    @Provides
    @Singleton
    fun provideUserSettingsSerializer(cryptoManager: CryptoManager) =
        UserSettingsSerializer(cryptoManager)
}