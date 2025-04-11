package com.nikol.wishlist.data

import androidx.core.os.BuildCompat
import com.nikol.wishlist.BuildConfig
import com.nikol.wishlist.domain.ImageUrlProvider
import com.nikol.wishlist.domain.ImageUrlProviderImpl
import dagger.Binds
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