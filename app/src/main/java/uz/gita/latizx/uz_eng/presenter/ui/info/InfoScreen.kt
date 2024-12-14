package uz.gita.latizx.uz_eng.presenter.ui.info

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.latizx.uz_eng.R
import uz.gita.latizx.uz_eng.databinding.ScreenInfoBinding
import kotlin.getValue

@AndroidEntryPoint
class InfoScreen : Fragment(R.layout.screen_info) {
    private val binding by viewBinding(ScreenInfoBinding::bind)
    private val viewModel: InfoContract.InfoViewModel by viewModels<InfoViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.apply {
            toolbar.setNavigationOnClickListener { viewModel.openPrevScreen() }
        }
    }

}