package uz.gita.latizx.uz_eng.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.latizx.uz_eng.presenter.navigator.AppNavigationDisputer
import uz.gita.latizx.uz_eng.presenter.navigator.AppNavigationHolder
import uz.gita.latizx.uz_eng.presenter.navigator.AppNavigator

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun bindAppNavigator(dispatcher: AppNavigationDisputer): AppNavigator

    @Binds
    fun bindAppNavigationHolder(dispatcher: AppNavigationDisputer): AppNavigationHolder
}