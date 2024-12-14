package uz.gita.latizx.uz_eng.presenter.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import uz.gita.latizx.uz_eng.data.source.local.PreferenceHelper
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModeImpl @Inject constructor(
    private val pref: PreferenceHelper,
    private val direction: OnboardingContract.OnBoardingDirection
) : OnboardingContract.OnboardingViewModel, ViewModel() {
    override val nextBtn = MutableSharedFlow<Unit>()
    override val backBtn = MutableSharedFlow<Unit>()

    init {
        if (!pref.isFirstLaunch()) viewModelScope.launch { direction.navigateToHome() }
    }

    override fun clickNextBtn() {
        viewModelScope.launch { nextBtn.emit(Unit) }
    }

    override fun clickBackBtn() {
        viewModelScope.launch { backBtn.emit(Unit) }
    }

    override fun openNextScreen() {
        viewModelScope.launch {
            direction.navigateToHome()
            pref.setFirstLaunch(false)
        }
    }
}