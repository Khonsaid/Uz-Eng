package uz.gita.latizx.uz_eng.presenter.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.latizx.uz_eng.data.model.DictionaryModel
import uz.gita.latizx.uz_eng.data.model.WordDetailModel
import uz.gita.latizx.uz_eng.domain.AppRepository

@HiltViewModel(assistedFactory = DetailViewModelImpl.Factory::class)
class DetailViewModelImpl @AssistedInject constructor(
    private val repository: AppRepository,
    private val direction: DetailContract.DetailDirection,
    @Assisted private val idData: Int
) : DetailContract.DetailViewModel, ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(idData: Int): DetailViewModelImpl
    }

    private var searJob: Job? = null
    private var dictionaryModel: DictionaryModel? = null
    override val wordDetail = MutableSharedFlow<List<WordDetailModel>>()
    override val word = MutableSharedFlow<DictionaryModel>()
    override val loadVolume = MutableSharedFlow<String>()
    override val loadSlowlyVolume = MutableSharedFlow<String>()
    override val showCopyMessage = MutableSharedFlow<String>()
    override val shareWord = MutableSharedFlow<String>()
    override val changeSaveState = MutableSharedFlow<Boolean>()

    init {
        searJob?.cancel()
        searJob = repository.getWordDetail(idData).onEach { result ->
            result.onSuccess {
                wordDetail.emit(it)
            }
            result.onFailure {
            }
        }.launchIn(viewModelScope)
        getDataById()
    }

    private fun getDataById() {
        viewModelScope.launch {
            repository.getWord(idData).onEach { result ->
                result.onSuccess {
                    word.emit(it)
                    dictionaryModel = it
                    changeSaveState.emit(dictionaryModel?.isFavourite == 1)
                }
                result.onFailure { }
            }.launchIn(viewModelScope)
        }
    }

    override fun clickShare() {
        viewModelScope.launch {
            dictionaryModel?.english?.let {
                shareWord.emit(it)
            }
        }
    }

    override fun clickSave() {
        viewModelScope.launch {
            dictionaryModel?.let {
                val isFav = it.isFavourite!!.xor(1)
                repository.updateFav(it.id, isFav).collect {
                    changeSaveState.emit(isFav == 1)
                    getDataById()
                }
            }
        }
    }

    override fun clickCopy() {
        viewModelScope.launch {
            dictionaryModel?.english?.let {
                showCopyMessage.emit(it)
            }
        }
    }

    override fun clickVolume() {
        viewModelScope.launch {
            dictionaryModel?.english?.let { loadVolume.emit(it) }
        }
    }

    override fun clickSlowly() {
        viewModelScope.launch {
            dictionaryModel?.english?.let { loadSlowlyVolume.emit(it) }
        }
    }

    override fun openPrevScreen() {
        viewModelScope.launch { direction.backToPrev() }
    }
}
