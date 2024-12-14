package uz.gita.latizx.uz_eng.presenter.ui.onboarding

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.gita.latizx.uz_eng.R
import uz.gita.latizx.uz_eng.databinding.ScreenOnboardingBinding
import uz.gita.latizx.uz_eng.presenter.ui.onboarding.adapter.OnboardingAdapter

@AndroidEntryPoint
class OnboardingScreen : Fragment(R.layout.screen_onboarding) {
    private val binding by viewBinding(ScreenOnboardingBinding::bind)
    private val viewModel by viewModels<OnboardingViewModeImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false

        binding.apply {
            viewPager.adapter = OnboardingAdapter(this@OnboardingScreen)
            dotsIndicator.attachTo(viewPager)

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    btnLeft.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    val colors = arrayOf(
                        ContextCompat.getColor(requireContext(), R.color.color3_onboarding),
                        ContextCompat.getColor(requireContext(), R.color.color2_onboarding),
                        ContextCompat.getColor(requireContext(), R.color.color1_onboarding),
                    )
                    if (position < colors.size - 1) {
                        val currentColor = colors[position]
                        val nextColor = colors[position + 1]

                        val blendedColor = blendColors(currentColor, nextColor, positionOffset)
                        window.statusBarColor = blendedColor
                        window.navigationBarColor = blendedColor
                        viewPager.setBackgroundColor(blendedColor)
                    }
                }
            })

            btnRight.setOnClickListener {
                if (viewPager.currentItem < 2) viewModel.clickNextBtn()
                else viewModel.openNextScreen()
            }
            btnLeft.setOnClickListener { viewPager.currentItem-- }

            lifecycleScope.launch {
                launch { viewModel.nextBtn.collect { binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true) } }
                launch { viewModel.backBtn.collect { binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true) } }
            }
        }
    }

    private fun blendColors(color1: Int, color2: Int, ratio: Float): Int {
        val inverseRatio = 1f - ratio

        val r = (Color.red(color1) * inverseRatio + Color.red(color2) * ratio).toInt()
        val g = (Color.green(color1) * inverseRatio + Color.green(color2) * ratio).toInt()
        val b = (Color.blue(color1) * inverseRatio + Color.blue(color2) * ratio).toInt()

        return Color.rgb(r, g, b)
    }
}