package com.daemon.tuzamate_v2.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.daemon.tuzamate_v2.MainActivity
import com.daemon.tuzamate_v2.databinding.FragmentSplashBinding
import androidx.navigation.findNavController
import com.daemon.tuzamate_v2.R

class SplashFragment : Fragment() {
    private lateinit var navController: NavController
    private var _binding: FragmentSplashBinding? = null
    private val binding: FragmentSplashBinding
        get() = requireNotNull(_binding){"FragmentSplashBinding -> null"}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).hideBottomNavigation(true)

        navController = view.findNavController()

        binding.root.setOnClickListener {
            navController.navigate(R.id.action_navigationp_splash_to_login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}