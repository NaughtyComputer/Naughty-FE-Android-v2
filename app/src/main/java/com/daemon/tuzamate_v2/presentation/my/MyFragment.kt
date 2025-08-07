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

class MyFragment : Fragment() {

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
        
        myViewModel.profileImage.observe(viewLifecycleOwner) { imageRes ->
            imageRes?.let {
                binding.profileImage.setImageResource(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}