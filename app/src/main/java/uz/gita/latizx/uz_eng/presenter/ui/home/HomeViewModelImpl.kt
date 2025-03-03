package uz.gita.latizx.uz_eng.presenter.ui.home

import android.content.Context
import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.gita.latizx.uz_eng.data.mapper.Mapper.getId
import uz.gita.latizx.uz_eng.data.model.DictionaryModel
import uz.gita.latizx.uz_eng.domain.AppRepository
import uz.gita.latizx.uz_eng.util.getClipboardText
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: AppRepository,
    private val direction: HomeContract.HomeDirection
) : HomeContract.HomeViewModel, ViewModel() {
    override val pasteData = MutableSharedFlow<String>()
    override val scrollToPosition = MutableSharedFlow<Int>()
    override val cursor = MutableStateFlow<Cursor?>(null)
    override val searchQuery = MutableStateFlow<String>("")
    override val isEng = MutableLiveData<Boolean>(true)
    override val notifyByPosition = MutableSharedFlow<Int>()

    override fun getCursor() {
        viewModelScope.launch {
            cursor.value = getCursorByLang()
        }
    }

    override fun searchByWord(word: String) {
        viewModelScope.launch {
            cursor.value = if (isEng.value!!) repository.searchByEngWord(word) else repository.searchByUzbWord(word)
            searchQuery.value = word
        }
    }

    override fun updateLang() {
        val changeLang = !isEng.value!!
        isEng.value = changeLang
        viewModelScope.launch { cursor.value = getCursorByLang() }
    }

    override fun updateFav(data: DictionaryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFav(data.id, data.isFavourite!!.xor(1)).collect {
//                cursor.value = if (isEng.value!!) repository.searchByEngWord(data.english!!) else repository.searchByUzbWord(data.uzbek!!)
//                notifyByPosition.emit(position)
                cursor.value = getCursorByLang()
//                scrollToPosition.emit(findPosition(cursor.value, data))
            }
        }
    }

    private suspend fun findPosition(cursor: Cursor?, data: DictionaryModel): Int {
        if (cursor == null) return RecyclerView.NO_POSITION
        for (position in 0 until cursor.count) {
            cursor.moveToPosition(position)
            if (cursor.getId() == data.id) {
                cursor.close()
                return position
            }
        }
        cursor.close()
        return RecyclerView.NO_POSITION
    }

    override fun openPaste() {
        viewModelScope.launch { pasteData.emit(context.getClipboardText()) }
    }

    override fun openFav() {
        viewModelScope.launch { direction.moveToSave() }
    }

    override fun openInfo() {
        viewModelScope.launch { direction.moveToInfo() }
    }

    override fun openDetail(dictionaryModel: DictionaryModel) {
        viewModelScope.launch { direction.moveToDetail(dictionaryModel) }
    }

//    private fun getClipboardText(): String {
//        val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
//        return clipboard.primaryClip?.getItemAt(0)?.text.toString()
//    }

    private suspend fun getCursorByLang(): Cursor = if (isEng.value!!) repository.getAllEng() else repository.getAllUzb()
}