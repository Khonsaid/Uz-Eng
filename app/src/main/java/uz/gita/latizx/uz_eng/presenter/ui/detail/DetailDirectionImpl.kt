package uz.gita.latizx.uz_eng.presenter.ui.detail

import uz.gita.latizx.uz_eng.presenter.navigator.AppNavigator
import javax.inject.Inject

class DetailDirectionImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : DetailContract.DetailDirection {
    override suspend fun backToPrev() {
        appNavigator.navigateBack()
    }
}