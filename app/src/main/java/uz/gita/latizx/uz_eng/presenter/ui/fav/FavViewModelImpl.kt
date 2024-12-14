package uz.gita.latizx.uz_eng.presenter.ui.fav

import android.database.Cursor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.gita.latizx.uz_eng.data.model.DictionaryModel
import uz.gita.latizx.uz_eng.domain.AppRepository
import javax.inject.Inject

@HiltViewModel
class FavViewModelImpl @Inject constructor(
    private val repository: AppRepository,
    private val direction: FavContract.FavDirection
) : FavContract.FavViewModel, ViewModel() {
    override val pasteData = MutableSharedFlow<String>()
    override val cursor = MutableStateFlow<Cursor?>(null)
    override val searchQuery = MutableStateFlow<String>("")
    override val notifyByPosition = MutableSharedFlow<Int>()

    override fun getCursor() {
        viewModelScope.launch { cursor.value = repository.getAllFav() }
    }

    override fun updateFav(id: Int, isFav: Int, position: Int) {
        viewModelScope.launch {
            repository.updateFav(id, isFav.xor(1)).collect {
                cursor.value = repository.getAllFav()
            }
        }
    }

    override fun clickDetail(dictionaryModel: DictionaryModel) {
        viewModelScope.launch { direction.moveToDetail(dictionaryModel) }
    }

    override fun openPrev() {
        viewModelScope.launch { direction.moveToPrev() }
    }
}