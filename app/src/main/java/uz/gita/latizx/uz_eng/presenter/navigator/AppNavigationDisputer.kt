package uz.gita.latizx.uz_eng.presenter.navigator

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigationDisputer @Inject constructor() : AppNavigator, AppNavigationHolder {
    override val navigationStack = MutableSharedFlow<NavController.() -> Unit>()

    private suspend fun navigate(args: NavigationArgs) {
        navigationStack.emit { args }
    }

    override suspend fun navigateTo(id: NavDirections) = navigate { navigate(id) }

    override suspend fun navigateBack() = navigate { navigateUp() }
}