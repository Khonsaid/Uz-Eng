package uz.gita.latizx.uz_eng.presenter.ui.onboarding

import uz.gita.latizx.uz_eng.presenter.navigator.AppNavigator
import javax.inject.Inject

class OnBoardingDirectionImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : OnboardingContract.OnBoardingDirection {

    override suspend fun navigateToHome() {
        appNavigator.navigateTo(OnboardingScreenDirections.actionOnboardingScreenToHomeScreen())
    }
}