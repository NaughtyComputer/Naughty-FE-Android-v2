package com.daemon.tuzamate_v2.presentation.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.daemon.tuzamate_v2.MainActivity
import com.daemon.tuzamate_v2.databinding.FragmentMyEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyEditFragment : Fragment() {

    private lateinit var navController: NavController
    private var _binding: FragmentMyEditBinding? = null
    private val binding: FragmentMyEditBinding
        get() = requireNotNull(_binding){"FragmentMyEditBinding -> null"}

    private val myViewModel: MyViewModel by activityViewModels()

    private var selectedProfileImage: Int? = null  // 모달에서 선택한 프로필 이미지 (임시 저장)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentMyEditBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        (requireActivity() as MainActivity).hideBottomNavigation(false)

        setupClickListeners()
        observeViewModel()
        
        myViewModel.loadProfile()
    }
    
    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.profileImageBox.setOnClickListener {
            val bottomSheet = MyEditBottomSheet { selectedImage ->
                selectedProfileImage = selectedImage
                myViewModel.setProfileImage(selectedImage)
                binding.profileImage.setImageResource(selectedImage)
            }
            bottomSheet.show(parentFragmentManager, "ProfileImageBottomSheet")
        }
    }
    
    private fun observeViewModel() {
        myViewModel.profileImage.observe(viewLifecycleOwner) { imageRes ->
            binding.profileImage.setImageResource(imageRes)
        }
        
        myViewModel.profileData.observe(viewLifecycleOwner) { profileResult ->
            profileResult?.let { updateUI(it) }
        }
        
        myViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // 로딩 상태 UI 처리 (필요시 프로그레스 바 추가)
        }
        
        myViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun updateUI(profileResult: com.daemon.tuzamate_v2.data.model.ProfileResult) {
        with(binding) {
            // 기본 정보
            nickname.text = profileResult.myInfo.nickname ?: "닉네임 없음"
            name.text = profileResult.myInfo.nickname ?: "이름 없음"
            gender.text = when(profileResult.myInfo.gender) {
                "MALE" -> "남"
                "FEMALE" -> "여"
                else -> "미설정"
            }
            email.text = profileResult.myInfo.email
            
            // 투자 정보
            income.text = profileResult.investmentInfo.income?.let { "${it}원" } ?: "미설정"
            purpose.text = profileResult.investmentInfo.purpose ?: "미설정"
            type.text = when(profileResult.investmentInfo.type) {
                "CONSERVATIVE" -> "안정"
                "MODERATE" -> "중간"
                "AGGRESSIVE" -> "위험"
                else -> "미설정"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}