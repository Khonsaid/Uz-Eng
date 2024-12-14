package uz.gita.latizx.uz_eng.presenter.ui.info

import uz.gita.latizx.uz_eng.presenter.navigator.AppNavigator
import javax.inject.Inject

class InfoDirectionImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : InfoContract.InfoDirection {
    override suspend fun navigateToBack() {
        appNavigator.navigateBack()
    }
}