package com.nikol.wishlist.core.data.network.di

import com.nikol.wishlist.core.data.network.AuthApiService
import com.nikol.wishlist.core.data.network.RetrofitClient
import com.nikol.wishlist.core.data.network.UserApiService
import com.nikol.wishlist.core.data.network.wishlist.WishlistApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideAuthApiService(retrofitClient: RetrofitClient): AuthApiService {
        return retrofitClient.createService(AuthApiService::class.java)
    }

//    @Provides
//    @Singleton
//    fun provideFriendsApiService(retrofitClient: RetrofitClient): FriendsApiService {
//        return retrofitClient.createService(FriendsApiService::class.java)
//    }

    @Provides
    @Singleton
    fun provideWishlistApiService(retrofitClient: RetrofitClient): WishlistApiService {
        return retrofitClient.createService(WishlistApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApiService(retrofitClient: RetrofitClient): UserApiService {
        return retrofitClient.createService(UserApiService::class.java)
    }
}