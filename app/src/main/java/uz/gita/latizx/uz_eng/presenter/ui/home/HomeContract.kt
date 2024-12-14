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
        fun updateFav(id: Int, isFav: Int, position: Int)
        fun clickPaste()
        fun clickDetail(dictionaryModel: DictionaryModel)
    }

    interface HomeDirection {
        suspend fun moveToDetail(dictionaryModel: DictionaryModel)
    }
}