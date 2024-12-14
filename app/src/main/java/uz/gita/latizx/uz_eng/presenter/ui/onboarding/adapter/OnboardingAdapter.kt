package uz.gita.latizx.uz_eng.presenter.ui.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.gita.latizx.uz_eng.presenter.ui.onboarding.pagers.Pager1
import uz.gita.latizx.uz_eng.presenter.ui.onboarding.pagers.Pager2
import uz.gita.latizx.uz_eng.presenter.ui.onboarding.pagers.Pager3

class OnboardingAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> Pager1()
            1 -> Pager2()
            else -> Pager3()
        }
}