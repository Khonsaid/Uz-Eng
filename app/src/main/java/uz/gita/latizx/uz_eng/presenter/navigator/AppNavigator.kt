package uz.gita.latizx.uz_eng.presenter.navigator

import androidx.navigation.NavDirections

interface AppNavigator {
    suspend fun navigateTo(id: NavDirections)
    suspend fun navigateBack()
}