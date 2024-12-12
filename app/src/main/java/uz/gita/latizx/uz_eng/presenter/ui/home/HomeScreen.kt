package uz.gita.latizx.uz_eng.presenter.ui.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.gita.latizx.uz_eng.R
import uz.gita.latizx.uz_eng.databinding.ScreenHomeBinding
import uz.gita.latizx.uz_eng.presenter.ui.home.adapter.WordAdapter
import uz.gita.latizx.uz_eng.util.SpeechToTextHelper
import uz.gita.latizx.uz_eng.util.TextToSpeechHealer
import uz.gita.latizx.uz_eng.util.copyToClipBoard
import java.util.Locale

@AndroidEntryPoint
class HomeScreen : Fragment(R.layout.screen_home) {
    private val binding by viewBinding(ScreenHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels<HomeViewModelImpl>()
    private val wordAdapter by lazy { WordAdapter() }
    private lateinit var ttsHelper: TextToSpeechHealer
    private lateinit var speechToTextHelper: SpeechToTextHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ttsHelper = TextToSpeechHealer(requireContext(), Locale.UK)
        speechToTextHelper = SpeechToTextHelper(requireContext())
        binding.fbHome.setOnClickListener {
            speechToTextHelper.startListening { results ->
                if (results.isNotEmpty()) {
                    binding.searchView.setText(results[0])
                    viewModel.searchByWord(results[0])
                }
            }
        }
        binding.apply {
            navView.setNavigationItemSelectedListener { menuItem ->
//                MenuItem.setChecked = true
                drawerLayout.close()

                if (menuItem.itemId == R.id.m_paste) viewModel.clickPaste()

                return@setNavigationItemSelectedListener true
            }
        }
        observers()
        settingsSearching()
        settingsAdapter()
        backPressed()
    }

    private fun settingsSearching() {
        binding.apply {
            searchBar.setNavigationOnClickListener { drawerLayout.open() }
            searchBar.setOnMenuItemClickListener { itemMenu ->
                when (itemMenu.itemId) {
                    R.id.m_paste -> {
                        viewModel.clickPaste()
                    }
                }
                return@setOnMenuItemClickListener true
            }
            searchView.setupWithSearchBar(searchBar)

            searchView.editText.setOnEditorActionListener { textView, i, keyEvent ->
                val text = textView?.text.toString().trim()
                searchView.hide()
                searchBar.setText(text)
                viewModel.searchByWord(text)
                false
            }
            searchView.editText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    binding.searchBar.setText("") // Matnni tozalaymiz
                }
            }
        }
    }

    private fun settingsAdapter() {
        binding.apply {
            rvHome.layoutManager = LinearLayoutManager(binding.root.context)
            rvHome.adapter = wordAdapter

            wordAdapter.apply {
                setCopyClickListener {
                    it?.english?.copyToClipBoard(requireContext())
                    Snackbar.make(binding.root, "${it?.english} copied", Snackbar.LENGTH_SHORT).show()
                }
                setShareClickListener { }
                setSlowlyClickListener { data -> data?.english?.let { viewModel.ttSlowlySpeech(it) } }
                setTTSClickLikeListener { data -> data?.english?.let { viewModel.ttSpeech(it) } }
                setSaveClickLikeListener { data -> data?.let { viewModel.updateFav(it.id, it.isFavourite!!) } }
            }
        }
    }

    private fun observers() {
        viewModel.pasteData.observe(viewLifecycleOwner) {
            binding.apply {
                searchView.show()
                searchView.setText(it)
                searchBar.setText(it)
                searchView.editText.setSelection(it.length)
            }
        }
        lifecycleScope.launch {
            launch { viewModel.cursor.collect { wordAdapter.submitCursor(it) } }
            launch {
                viewModel.ttSpeech.collect {
                    ttsHelper.setPitch()
                    ttsHelper.setSpeechRate()
                    ttsHelper.speak(it)
                }
            }
            launch {
                viewModel.ttSlowlySpeech.collect {
                    ttsHelper.setPitch(0.6f)
                    ttsHelper.setSpeechRate(0.5f)
                    ttsHelper.speak(it)
                }
            }
        }
    }

    private fun backPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.searchView.isShowing) {
                    binding.searchView.hide() // `SearchView`ni yopamiz
                } else {
                    isEnabled = false // Callbackni o'chiramiz
                    requireActivity().onBackPressed() // Standart xatti-harakatni davom ettiramiz
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        ttsHelper.stop()
        ttsHelper.shutdown()

        speechToTextHelper.stopListening()
        speechToTextHelper.destroy()
    }
}