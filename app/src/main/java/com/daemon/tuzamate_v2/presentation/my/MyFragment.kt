package com.daemon.tuzamate_v2.presentation.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.daemon.tuzamate_v2.MainActivity
import com.daemon.tuzamate_v2.R
import com.daemon.tuzamate_v2.databinding.FragmentMyBinding
import com.daemon.tuzamate_v2.utils.CreditManager
import androidx.lifecycle.lifecycleScope
import androidx.core.os.bundleOf
import com.daemon.tuzamate_v2.presentation.contents.PlazaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFragment : Fragment() {
    
    @Inject
    lateinit var creditManager: CreditManager

    private lateinit var navController: NavController
    private var _binding: FragmentMyBinding? = null
    private val binding: FragmentMyBinding
        get() = requireNotNull(_binding){"FragmentMyBinding -> null"}
    
    private val myViewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentMyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        (requireActivity() as MainActivity).hideBottomNavigation(false)

        binding.btnSetting.setOnClickListener {
            navController.navigate(R.id.action_navigation_my_to_edit)
        }
        
//        binding.btnApiTest.setOnClickListener {
//            navController.navigate(R.id.navigation_api_test)
//        }

        binding.btnPostWrite.setOnClickListener {
            val bundle = bundleOf(
                "contentType" to PlazaViewModel.ContentType.MY_POSTS.name
            )
            navController.navigate(R.id.navigation_plaza, bundle)
        }

        binding.btnPostLike.setOnClickListener {
            val bundle = bundleOf(
                "contentType" to PlazaViewModel.ContentType.MY_LIKES.name
            )
            navController.navigate(R.id.navigation_plaza, bundle)
        }

        binding.btnPostScrap.setOnClickListener {
            val bundle = bundleOf(
                "contentType" to PlazaViewModel.ContentType.MY_SCRAPS.name
            )
            navController.navigate(R.id.navigation_plaza, bundle)
        }

        observeViewModel()
        observeCredit()
    }
    
    override fun onResume() {
        super.onResume()
        // MyEditFragment에서 돌아왔을 때 프로필 정보 다시 로드
        myViewModel.loadProfile()
    }
    
    private fun observeViewModel() {
        myViewModel.profileImage.observe(viewLifecycleOwner) { imageRes ->
            imageRes?.let {
                binding.profileImage.setImageResource(it)
            }
        }

        myViewModel.profileData.observe(viewLifecycleOwner) { profileResult ->
            profileResult?.let {
                binding.nickname.text = it.myInfo.nickname ?: "닉네임 없음"
            }
        }

    }
    
    private fun observeCredit() {
        lifecycleScope.launch {
            creditManager.creditFlow.collect { credit ->
                _binding?.let {
                    it.amountCredit.text = credit.toString()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}