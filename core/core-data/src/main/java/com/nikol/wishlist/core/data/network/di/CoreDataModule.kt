package com.nikol.wishlist.core.data.network.di

import com.nikol.wishlist.core.data.network.AuthInteractorImpl
import com.nikol.wishlist.core.data.network.UserInteractorImpl
import com.nikol.wishlist.core.data.network.wishlist.WishlistsInteractorImpl
import com.nikol.wishlist.core.domain.auth.AuthInteractor
import com.nikol.wishlist.core.domain.user.UserInteractor
import com.nikol.wishlist.core.domain.wishlist.WishlistsInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn

@Module
@InstallIn(dagger.hilt.components.SingletonComponent::class)
abstract class CoreDataModule {
    @Binds
    abstract fun bindAuthInteractor(impl: AuthInteractorImpl): AuthInteractor

    @Binds
    abstract fun bindUserInteractor(impl: UserInteractorImpl): UserInteractor

    @Binds
    abstract fun bindWishlistInteractor(impl: WishlistsInteractorImpl): WishlistsInteractor
}