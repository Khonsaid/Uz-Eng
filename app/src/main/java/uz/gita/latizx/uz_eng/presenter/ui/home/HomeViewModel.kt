package uz.gita.latizx.uz_eng.presenter.ui.home

import android.database.Cursor
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {
    val pasteData: LiveData<String>
    val cursor: StateFlow<Cursor?>
    val searchQuery: StateFlow<String>
    val isEng: LiveData<Boolean>

    fun searchByWord(word: String)
    fun updateLang()
    fun updateFav(id: Int, isFav: Int)
    fun clickPaste()
}