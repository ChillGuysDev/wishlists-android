package com.nikol.wishlist.core.data.network.di

import com.nikol.wishlist.core.data.network.tokenprovider.DataStoreTokenProvider
import com.nikol.wishlist.core.domain.tokenprovider.AuthTokenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthTokenProvider(
        keystoreTokenProvider: DataStoreTokenProvider
    ): AuthTokenProvider
}