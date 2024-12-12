package uz.gita.latizx.uz_eng.presenter.ui.history

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.gita.latizx.uz_eng.domain.AppRepository
import javax.inject.Inject

@HiltViewModel
class HistoryViewModelImpl @Inject constructor(
    private val repository: AppRepository,
    private val direction: HistoryContract.HistoryDirection
) : HistoryContract.HistoryViewModel, ViewModel() {

}