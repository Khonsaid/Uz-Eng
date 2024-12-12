package uz.gita.latizx.uz_eng.presenter.navigator

import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow

typealias NavigationArgs = NavController.() -> Unit

interface AppNavigationHolder {
    val navigationStack: Flow<NavigationArgs>
}