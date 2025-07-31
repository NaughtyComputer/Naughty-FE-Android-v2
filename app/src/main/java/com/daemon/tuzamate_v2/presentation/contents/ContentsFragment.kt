package com.daemon.tuzamate_v2.presentation.contents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.daemon.tuzamate_v2.MainActivity
import com.daemon.tuzamate_v2.R
import com.daemon.tuzamate_v2.databinding.FragmentContentsBinding
import com.daemon.tuzamate_v2.utils.TabPagerAdapter

class ContentsFragment : Fragment() {

    private lateinit var navController: NavController
    private var _binding: FragmentContentsBinding? = null
    private val binding: FragmentContentsBinding
        get() = requireNotNull(_binding){"FragmentContentsBinding -> null"}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentContentsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        (requireActivity() as MainActivity).hideBottomNavigation(false)

        binding.btnCredit.setOnClickListener {
            navController.navigate(R.id.action_navigation_contents_to_my_credit)
        }

        binding.bannerBox.setOnClickListener {
            navController.navigate(R.id.action_navigation_contents_to_article)
        }

        setupViewPager()
        setupCustomTabBar()
    }

    private fun setupViewPager() {
        val adapter = TabPagerAdapter(this.requireActivity())
        binding.viewPager.adapter = adapter

        // ViewPager2와 CustomTabBar 연동
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.customTabBar.setSelectedTab(position)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.customTabBar.updateIndicatorPosition(position, positionOffset)
            }
        })
    }

    private fun setupCustomTabBar() {
        binding.customTabBar.setOnTabSelectedListener { index ->
            binding.viewPager.currentItem = index
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}