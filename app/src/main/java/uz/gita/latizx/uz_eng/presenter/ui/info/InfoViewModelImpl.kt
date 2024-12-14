package uz.gita.latizx.uz_eng.presenter.ui.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.gita.latizx.uz_eng.domain.AppRepository
import javax.inject.Inject

@HiltViewModel
class InfoViewModelImpl @Inject constructor(
    private val direction: InfoContract.InfoDirection
) : InfoContract.InfoViewModel, ViewModel() {

    override fun openPrevScreen() {
        viewModelScope.launch { direction.navigateToBack() }
    }
}