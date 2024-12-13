package uz.gita.latizx.uz_eng.presenter.ui.home

import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.gita.latizx.uz_eng.domain.AppRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: AppRepository
) : HomeViewModel, ViewModel() {
    override val pasteData = MutableLiveData<String>()
    override val cursor = MutableStateFlow<Cursor?>(null)
    override val searchQuery = MutableStateFlow<String>("")
    override val isEng = MutableLiveData<Boolean>(true)

    init {
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

    override fun updateFav(id: Int, isFav: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFav(id, isFav.xor(1)).collect {
                cursor.value = getCursorByLang()
            }
        }
    }

    override fun clickPaste() {
        pasteData.value = getClipboardText()
    }

    private fun getClipboardText(): String? {
        val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        return clipboard.primaryClip?.getItemAt(0)?.text.toString()
    }

    private suspend fun getCursorByLang(): Cursor = if (isEng.value!!) repository.getAllEng() else repository.getAllUzb()
}