package uz.gita.latizx.uz_eng.presenter.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.gita.latizx.uz_eng.R
import uz.gita.latizx.uz_eng.databinding.ScreenHomeBinding
import uz.gita.latizx.uz_eng.presenter.ui.home.adapter.WordAdapter
import uz.gita.latizx.uz_eng.util.SpeechToTextHelper
import uz.gita.latizx.uz_eng.util.copyToClipBoard
import uz.gita.latizx.uz_eng.util.shareText

@AndroidEntryPoint
class HomeScreen : Fragment(R.layout.screen_home) {
    private val binding by viewBinding(ScreenHomeBinding::bind)
    private val viewModel: HomeContract.HomeViewModel by viewModels<HomeViewModelImpl>()
    private val wordAdapter by lazy { WordAdapter() }
    private lateinit var speechToTextHelper: SpeechToTextHelper
    private val REQUEST_CODE = 100

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        speechToTextHelper = SpeechToTextHelper(requireContext(), binding.fbHome)
        viewModel.getCursor()

        binding.apply {
            navView.setNavigationItemSelectedListener { menuItem ->
                drawerLayout.close()
                if (menuItem.itemId == R.id.m_paste) viewModel.clickPaste()
                return@setNavigationItemSelectedListener true
            }
        }
        setupFloatingBalloon()
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
                    R.id.m_paste -> viewModel.clickPaste()
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
                if (!hasFocus) resetSearchView()
            }
        }
    }

    private fun settingsAdapter() {
        binding.apply {
            rvHome.layoutManager = LinearLayoutManager(binding.root.context)
            rvHome.adapter = wordAdapter

            wordAdapter.apply {
                setCopyClickListener { it?.english?.copyToClipBoard(requireContext()) }
                setShareClickListener { requireActivity().shareText(it?.english) }
                setSaveClickLikeListener { data -> data?.let { viewModel.updateFav(it.id, it.isFavourite!!) } }
                setDetailClickListener { data -> data?.let { viewModel.clickDetail(it) } }
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
            launch {
                viewModel.cursor.collect {
                    binding.apply {
                        imgSearch.visibility = if ((it?.count ?: 0) > 0) View.GONE else View.VISIBLE
                        tvSearch.visibility = if ((it?.count ?: 0) > 0) View.GONE else View.VISIBLE
                    }
                    wordAdapter.submitCursor(it)
                }
            }
            launch { viewModel.searchQuery.collect { wordAdapter.searchQuery(it) } }
        }
    }

    private fun setupFloatingBalloon() {
        val balloon = Balloon.Builder(requireContext())
            .setText("Voice search activated!")
            .setTextColorResource(R.color.black)
            .setBackgroundColorResource(R.color.toast)
            .setTextSize(14f)
            .setPadding(8)
            .setCornerRadius(8f)
            .setBalloonAnimation(BalloonAnimation.FADE)
            .setDismissWhenClicked(true)
            .setDismissWhenTouchOutside(true)
            .setAutoDismissDuration(4000L) // 4 soniya o'tib avtomatik o'chadi
            .build()

        binding.fbHome.setOnClickListener {
            checkAudioPermission()
            balloon.showAlignTop(binding.fbHome)

            speechToTextHelper.startListening { results ->
                if (results.isNotEmpty()) {
                    balloon.dismiss()
                    binding.searchView.setText(results[0])
                    viewModel.searchByWord(results[0])
                }
            }
        }
    }

    private fun checkAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // M = 23
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_CODE)
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
        speechToTextHelper.stopListening()
        speechToTextHelper.destroy()
        resetSearchView() // Reset search view when the fragment goes to the background
    }

    private fun resetSearchView() {
        binding.apply {
            searchBar.setText("") // Clear the search bar text
            searchView.setText("") // Clear the search view text
            searchView.hide() // Hide the search view
        }
    }
}