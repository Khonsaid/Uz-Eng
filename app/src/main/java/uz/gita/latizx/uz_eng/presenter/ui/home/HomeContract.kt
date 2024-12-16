package uz.gita.latizx.uz_eng.presenter.ui.home

import android.database.Cursor
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import uz.gita.latizx.uz_eng.data.model.DictionaryModel

interface HomeContract {
    interface HomeViewModel {
        val pasteData: SharedFlow<String>
        val cursor: StateFlow<Cursor?>
        val searchQuery: StateFlow<String>
        val isEng: LiveData<Boolean>
        val notifyByPosition: SharedFlow<Int>

        fun getCursor()
        fun searchByWord(word: String)
        fun updateLang()
        fun updateFav(dictionaryModel: DictionaryModel)
        fun openPaste()
        fun openFav()
        fun openInfo()
        fun openDetail(dictionaryModel: DictionaryModel)
    }

    interface HomeDirection {
        suspend fun moveToDetail(dictionaryModel: DictionaryModel)
        suspend fun moveToSave()
        suspend fun moveToInfo()
    }
}