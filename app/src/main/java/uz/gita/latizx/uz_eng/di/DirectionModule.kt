package uz.gita.latizx.uz_eng.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.latizx.uz_eng.presenter.ui.detail.DetailContract
import uz.gita.latizx.uz_eng.presenter.ui.detail.DetailDirectionImpl
import uz.gita.latizx.uz_eng.presenter.ui.fav.FavContract
import uz.gita.latizx.uz_eng.presenter.ui.fav.FavDirectionImpl
import uz.gita.latizx.uz_eng.presenter.ui.home.HomeContract
import uz.gita.latizx.uz_eng.presenter.ui.home.HomeDirectionImpl
import uz.gita.latizx.uz_eng.presenter.ui.info.InfoContract
import uz.gita.latizx.uz_eng.presenter.ui.info.InfoDirectionImpl
import uz.gita.latizx.uz_eng.presenter.ui.onboarding.OnBoardingDirectionImpl
import uz.gita.latizx.uz_eng.presenter.ui.onboarding.OnboardingContract

@Module
@InstallIn(ViewModelComponent::class)
interface DirectionModule {

    @Binds
    fun bindDetailDirection(impl: DetailDirectionImpl): DetailContract.DetailDirection

    @Binds
    fun bindHistoryDirection(impl: FavDirectionImpl): FavContract.FavDirection

    @Binds
    fun bindInfoDirection(impl: InfoDirectionImpl): InfoContract.InfoDirection

    @Binds
    fun bindHomeDirection(impl: HomeDirectionImpl): HomeContract.HomeDirection

    @Binds
    fun bindOnboardingDirection(impl: OnBoardingDirectionImpl): OnboardingContract.OnBoardingDirection
}