package uz.gita.latizx.uz_eng.presenter.ui.fav

import uz.gita.latizx.uz_eng.data.model.DictionaryModel
import uz.gita.latizx.uz_eng.presenter.navigator.AppNavigator
import javax.inject.Inject

class FavDirectionImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : FavContract.FavDirection {
    override suspend fun moveToDetail(dictionaryModel: DictionaryModel) {
        appNavigator.navigateTo(FavScreenDirections.actionHistoryScreen2ToDetailScreen(dictionaryModel.id))
    }

    override suspend fun moveToPrev() {
        appNavigator.navigateBack()
    }
}