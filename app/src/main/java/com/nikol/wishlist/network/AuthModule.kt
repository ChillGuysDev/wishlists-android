package com.nikol.wishlist.network

import com.nikol.wishlist.network.tokenprovider.AuthTokenProvider
import com.nikol.wishlist.network.tokenprovider.KeystoreTokenProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthTokenProvider(
        keystoreTokenProvider: KeystoreTokenProvider
    ): AuthTokenProvider
}