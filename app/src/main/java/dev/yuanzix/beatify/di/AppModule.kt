package dev.yuanzix.beatify.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.yuanzix.beatify.data.DataStoreRepository
import dev.yuanzix.beatify.data.authRepository.AuthRepository
import dev.yuanzix.beatify.data.musicRepository.MusicRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthRepository() = AuthRepository()

    @Provides
    @Singleton
    fun provideMusicRepository() = MusicRepository()

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext ctx: Context,
    ) = DataStoreRepository(ctx)
}