package uz.gita.latizx.uz_eng.presenter.ui.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.appbar.AppBarLayout
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.launch
import uz.gita.latizx.uz_eng.R
import uz.gita.latizx.uz_eng.data.model.DictionaryModel
import uz.gita.latizx.uz_eng.databinding.ScreenDetailBinding
import uz.gita.latizx.uz_eng.presenter.ui.detail.adapter.DetailAdapter
import uz.gita.latizx.uz_eng.util.TextToSpeechHealer
import uz.gita.latizx.uz_eng.util.copyToClipBoard
import uz.gita.latizx.uz_eng.util.shareText
import uz.gita.latizx.uz_eng.util.startScaleAnim
import uz.gita.latizx.uz_eng.util.stopScaleAnim
import javax.inject.Inject
import kotlin.getValue
import kotlin.math.abs

@AndroidEntryPoint
class DetailScreen : Fragment(R.layout.screen_detail) {
    @Inject
    lateinit var ttsHelper: TextToSpeechHealer
    private val args: DetailScreenArgs by navArgs()
    private val binding by viewBinding(ScreenDetailBinding::bind)
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var balloon: Balloon
    private val viewModel: DetailContract.DetailViewModel by viewModels<DetailViewModelImpl>(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<DetailViewModelImpl.Factory> { factory -> factory.create(args.idData) }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ttsHelper = TextToSpeechHealer(requireActivity())

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.apply {
            toolbar.setNavigationOnClickListener { viewModel.openPrevScreen() }
            rv.layoutManager = LinearLayoutManager(requireActivity())
            btnFast.setOnClickListener { viewModel.clickVolume() }
            btnShare.setOnClickListener { viewModel.clickShare() }
            btnSave.setOnClickListener { viewModel.clickSave() }
            btnCopy.setOnClickListener { viewModel.clickCopy() }
        }
        observers()
    }

    private fun observers() {
        lifecycleScope.launch {
            launch { viewModel.word.collect { loadWordData(it) } }
            launch { viewModel.shareWord.collect { requireActivity().shareText(it) } }
            launch {
                viewModel.wordDetail.collect {
                    detailAdapter = DetailAdapter(it)
                    binding.apply {
                        rv.adapter = detailAdapter
                        if (it.isNotEmpty()) {
                            imgBook.visibility = View.GONE
                            tvBook.visibility = View.GONE
                        }
                    }
                }
            }
            launch {
                viewModel.loadVolume.collect {
                    binding.btnFast.startScaleAnim()
                    ttsHelper.setPitch(1f)
                    ttsHelper.setSpeechRate(1f)
                    ttsHelper.speak(it, onStop = { binding.btnFast.stopScaleAnim() })
                }
            }
            launch {
                viewModel.showCopyMessage.collect {
                    it.copyToClipBoard(requireActivity())
                    balloon = Balloon.Builder(binding.root.context)
                        .setText("$it copied")
                        .setTextSize(12f)
                        .setTextColorResource(R.color.black)
                        .setBackgroundColorResource(R.color.toast)
                        .setArrowSize(10)
                        .setArrowPosition(0.5f)
                        .setArrowOrientation(ArrowOrientation.TOP)
                        .setCornerRadius(8f)
                        .setPadding(8)
                        .setAutoDismissDuration(1500L)
                        .setMargin(4)
                        .build()

                    balloon.showAlignTop(binding.btnCopy)
                }
            }
            launch {
                viewModel.changeSaveState.collect {
                    binding.btnSave.isSelected = it
                }
            }
        }
    }

    private fun loadWordData(data: DictionaryModel) {
        binding.apply {
            tvWord.text = data.english
            tvTrans.text = data.uzbek

            collapsingToolbar.title = ""
            collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedTitleStyle)
            collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedTitleStyle)

            appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBar, verticalOffset ->
                val isCollapsed = abs(verticalOffset) == appBar.totalScrollRange
                if (isCollapsed) collapsingToolbar.title = data.english
                else collapsingToolbar.title = ""
            })
        }
    }
}
