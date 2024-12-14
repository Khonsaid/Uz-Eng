package uz.gita.latizx.uz_eng.presenter.ui.home

import uz.gita.latizx.uz_eng.data.model.DictionaryModel
import uz.gita.latizx.uz_eng.presenter.navigator.AppNavigator
import javax.inject.Inject

class HomeDirectionImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : HomeContract.HomeDirection {
    override suspend fun moveToDetail(dictionaryModel: DictionaryModel) {
        appNavigator.navigateTo(HomeScreenDirections.actionHomeScreenToDetailScreen(dictionaryModel.id))
    }

    override suspend fun moveToSave() {
        appNavigator.navigateTo(HomeScreenDirections.actionHomeScreenToFavScreen2())
    }

    override suspend fun moveToInfo() {
        appNavigator.navigateTo(HomeScreenDirections.actionHomeScreenToInfoScreen())
    }
}