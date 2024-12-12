package uz.gita.latizx.uz_eng.presenter.ui.info

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.gita.latizx.uz_eng.domain.AppRepository
import javax.inject.Inject

@HiltViewModel
class InfoViewModelImpl @Inject constructor(
    private val repository: AppRepository,
    private val direction: InfoContract.InfoDirection
) : InfoContract.InfoViewModel, ViewModel() {

}