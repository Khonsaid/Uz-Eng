package uz.gita.latizx.uz_eng.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.latizx.uz_eng.domain.AppRepository
import uz.gita.latizx.uz_eng.domain.AppRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindRepository(impl: AppRepositoryImpl): AppRepository
}