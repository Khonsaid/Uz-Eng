package uz.gita.latizx.uz_eng.presenter.ui.fav

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.gita.latizx.uz_eng.R
import uz.gita.latizx.uz_eng.databinding.ScreenFavBinding
import uz.gita.latizx.uz_eng.presenter.ui.home.adapter.WordAdapter
import uz.gita.latizx.uz_eng.util.copyToClipBoard
import uz.gita.latizx.uz_eng.util.shareText
import kotlin.getValue

@AndroidEntryPoint
class FavScreen : Fragment(R.layout.screen_fav) {
    private val binding by viewBinding(ScreenFavBinding::bind)
    private val viewModel: FavContract.FavViewModel by viewModels<FavViewModelImpl>()
    private val wordAdapter by lazy { WordAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getCursor()
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.apply {
            toolbar.setNavigationOnClickListener { viewModel.openPrev() }
            rvHome.layoutManager = LinearLayoutManager(requireContext())
            rvHome.adapter = wordAdapter
        }
        wordAdapter.apply {
            setCopyClickListener { it?.english?.copyToClipBoard(requireContext()) }
            setShareClickListener { requireContext().shareText(it?.english) }
            setDetailClickListener { data -> data?.let { viewModel.clickDetail(it) } }
            setSaveClickLikeListener { data, position ->
                data?.let {
                    viewModel.updateFav(it.id, it.isFavourite!!, position)
                }
            }
        }
        observers()
    }

    private fun observers() {
        lifecycleScope.launch {
            launch {
                viewModel.cursor.collect {
                    binding.apply {
                        imgSearch.visibility = if ((it?.count ?: 0) > 0) View.GONE else View.VISIBLE
                        tvSearch.visibility = if ((it?.count ?: 0) > 0) View.GONE else View.VISIBLE
                    }
                    wordAdapter.submitCursor(it)
                }
            }
        }
    }
}