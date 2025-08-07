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
import com.daemon.tuzamate_v2.databinding.FragmentMyEditBinding
import kotlin.getValue

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

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.profileImageBox.setOnClickListener {
            val bottomSheet = MyEditBottomSheet { selectedImage ->
                selectedProfileImage = selectedImage  // 선택한 이미지 임시 저장
                myViewModel.setProfileImage(selectedImage)  // ViewModel에 저장
                binding.profileImage.setImageResource(selectedImage)
            }
            bottomSheet.show(parentFragmentManager, "ProfileImageBottomSheet")
        }

        myViewModel.profileImage.observe(viewLifecycleOwner) { imageRes ->
            binding.profileImage.setImageResource(imageRes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}