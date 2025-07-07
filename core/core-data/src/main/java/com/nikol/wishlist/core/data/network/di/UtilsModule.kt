package com.nikol.wishlist.core.data.network.di

import com.nikol.wishlist.core.data.BuildConfig
import com.nikol.wishlist.core.domain.ImageUrlProvider
import com.nikol.wishlist.core.domain.ImageUrlProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {
    @Provides
    @Singleton
    fun provideImageUrlProvider(
    ): ImageUrlProvider {
        return ImageUrlProviderImpl(BuildConfig.BASE_URL_IMAGE)
    }
}