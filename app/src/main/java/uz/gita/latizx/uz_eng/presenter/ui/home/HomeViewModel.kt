package uz.gita.latizx.uz_eng.presenter.ui.home

import android.database.Cursor
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {
    val pasteData: LiveData<String>
    val cursor: StateFlow<Cursor?>
    val ttSpeech: SharedFlow<String>
    val ttSlowlySpeech: SharedFlow<String>
    val isEng: LiveData<Boolean>
    //    val cursor: Cursor?

    fun searchByWord(word: String)
    fun updateLang()
    fun updateFav(id: Int, isFav: Int)
    fun clickPaste()
    fun ttSpeech(word: String)
    fun ttSlowlySpeech(word: String)
}