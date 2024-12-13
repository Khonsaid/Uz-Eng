package uz.gita.latizx.uz_eng.presenter.ui.detail

import kotlinx.coroutines.flow.SharedFlow
import uz.gita.latizx.uz_eng.data.model.DictionaryModel
import uz.gita.latizx.uz_eng.data.model.WordDetailModel

interface DetailContract {
    interface DetailViewModel {
        val wordDetail: SharedFlow<List<WordDetailModel>>
        val word: SharedFlow<DictionaryModel>
        val loadVolume: SharedFlow<String>
        val showCopyMessage: SharedFlow<String>
        val shareWord: SharedFlow<String>
        val changeSaveState: SharedFlow<Boolean>

        fun clickShare()
        fun clickSave()
        fun clickCopy()
        fun clickVolume()
        fun openPrevScreen()
    }

    interface DetailDirection {
        suspend fun backToPrev()
    }
}