package uz.gita.latizx.uz_eng.presenter.ui.detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.gita.latizx.uz_eng.domain.AppRepository
import javax.inject.Inject

@HiltViewModel
class DetailViewModelImpl @Inject constructor(
    private val repository: AppRepository,
    private val direction: DetailContract.DetailDirection
) : DetailContract.DetailViewModel, ViewModel() {
}