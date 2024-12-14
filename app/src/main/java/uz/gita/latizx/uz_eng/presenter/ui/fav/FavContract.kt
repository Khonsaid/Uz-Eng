package uz.gita.latizx.uz_eng.presenter.ui.fav

import android.database.Cursor
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import uz.gita.latizx.uz_eng.data.model.DictionaryModel

interface FavContract {
    interface FavViewModel{
        val pasteData: SharedFlow<String>
        val cursor: StateFlow<Cursor?>
        val searchQuery: StateFlow<String>
        val notifyByPosition: SharedFlow<Int>

        fun getCursor()
        fun updateFav(id: Int, isFav: Int, position: Int)
        fun clickDetail(dictionaryModel: DictionaryModel)
        fun openPrev()
    }
    interface FavDirection{
        suspend fun moveToDetail(dictionaryModel: DictionaryModel)
        suspend fun moveToPrev()
    }
}