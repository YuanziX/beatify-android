package dev.yuanzix.beatify.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.yuanzix.beatify.data.DataStoreRepository
import dev.yuanzix.beatify.data.auth_repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLoginSignupRepository() = AuthRepository()

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext ctx: Context,
    ) = DataStoreRepository(ctx)
}