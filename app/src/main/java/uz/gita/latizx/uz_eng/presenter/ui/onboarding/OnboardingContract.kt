package uz.gita.latizx.uz_eng.presenter.ui.onboarding

import kotlinx.coroutines.flow.SharedFlow

interface OnboardingContract {
    interface OnboardingViewModel {
        val nextBtn: SharedFlow<Unit>
        val backBtn: SharedFlow<Unit>

        fun clickNextBtn()
        fun clickBackBtn()
        fun openNextScreen()
    }

    interface OnBoardingDirection {
        suspend fun navigateToHome()
    }
}