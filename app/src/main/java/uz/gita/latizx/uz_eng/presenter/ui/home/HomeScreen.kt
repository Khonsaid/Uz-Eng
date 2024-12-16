package uz.gita.latizx.uz_eng.presenter.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
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
                when (menuItem.itemId) {
                    R.id.m_paste -> viewModel.openPaste()
                    R.id.favScreen -> viewModel.openFav()
                    R.id.infoScreen -> viewModel.openInfo()
                    R.id.settingsScreen -> showToast()
                    R.id.rateApp -> showToast()
                }
                return@setNavigationItemSelectedListener false
            }
            btnDraw.setOnClickListener {
                drawerLayout.open()
                hideKeyboard()
            }
        }
        setupFloatingBalloon()
        observers()
        settingsSearching()
        settingsAdapter()
        backPressed()
    }

    private fun showToast() {
        Toast.makeText(requireContext(), "Soon!", Toast.LENGTH_SHORT).show()
    }

    private fun settingsSearching() {
        binding.apply {
            btnPaste.setOnClickListener { viewModel.openPaste() }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.searchByWord(query ?: "")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.searchByWord(newText ?: "")
                    return true
                }
            })
        }
    }

    private fun settingsAdapter() {
        binding.apply {
            rvHome.layoutManager = LinearLayoutManager(binding.root.context)
            rvHome.adapter = wordAdapter

            wordAdapter.apply {
                setCopyClickListener { it?.english?.copyToClipBoard(requireContext()) }
                setShareClickListener { requireActivity().shareText(it?.english) }
                setSaveClickLikeListener { data, _ ->
                    data?.let { viewModel.updateFav(it) }
                }
                setDetailClickListener { data -> data?.let { viewModel.openDetail(it) } }
            }
        }
    }

    private fun observers() {
        lifecycleScope.launch {
            launch { viewModel.searchQuery.collect { wordAdapter.searchQuery(it) } }
            launch { viewModel.notifyByPosition.collect { wordAdapter.notifyItemChanged(it) } }
            launch { viewModel.pasteData.collect { binding.searchView.setQuery(it, false) } }
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

    private fun setupFloatingBalloon() {
        val balloon = Balloon.Builder(requireContext())
            .setText("Voice search\nactivated!")
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
                    binding.searchView.setQuery(results[0], false)
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

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = requireActivity().currentFocus
        if (currentFocusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusedView.windowToken, 0)
        }
    }

    private fun backPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.searchView.query.isNotEmpty()) {
                    binding.searchView.setQuery("", false)
                    viewModel.getCursor()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        speechToTextHelper.stopListening()
        speechToTextHelper.destroy()
    }
}