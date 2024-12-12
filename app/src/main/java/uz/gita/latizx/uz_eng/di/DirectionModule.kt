package uz.gita.latizx.uz_eng.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.latizx.uz_eng.presenter.ui.detail.DetailContract
import uz.gita.latizx.uz_eng.presenter.ui.detail.DetailDirectionImpl
import uz.gita.latizx.uz_eng.presenter.ui.history.HistoryContract
import uz.gita.latizx.uz_eng.presenter.ui.history.HistoryDirectionImpl
import uz.gita.latizx.uz_eng.presenter.ui.info.InfoContract
import uz.gita.latizx.uz_eng.presenter.ui.info.InfoDirectionImpl

@Module
@InstallIn(ViewModelComponent::class)
interface DirectionModule {

    @Binds
    fun bindDetailDirection(impl: DetailDirectionImpl): DetailContract.DetailDirection

    @Binds
    fun bindHistoryDirection(impl: HistoryDirectionImpl): HistoryContract.HistoryDirection

    @Binds
    fun bindInfoDirection(impl: InfoDirectionImpl): InfoContract.InfoDirection

}