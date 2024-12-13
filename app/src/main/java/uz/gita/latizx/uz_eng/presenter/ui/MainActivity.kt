package uz.gita.latizx.uz_eng.presenter.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.latizx.uz_eng.R
import uz.gita.latizx.uz_eng.databinding.ActivityMainBinding
import uz.gita.latizx.uz_eng.presenter.navigator.AppNavigationHolder
import uz.gita.latizx.uz_eng.util.setColor
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var appNavigationHolder: AppNavigationHolder
    private val binding by viewBinding(ActivityMainBinding::bind)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.setColor(R.color.primary)

        val navHost = binding.navHostFragmentContainer.getFragment<NavHostFragment>()
        val navController = navHost.navController
        appNavigationHolder.navigationStack.onEach {
            it(navController)
        }.launchIn(lifecycleScope)
    }
}