package com.daemon.tuzamate_v2.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.daemon.tuzamate_v2.presentation.contents.NewsLetterFragment
import com.daemon.tuzamate_v2.presentation.contents.PlazaFragment

class TabPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewsLetterFragment()
            1 -> PlazaFragment()
            else -> NewsLetterFragment()
        }
    }
}