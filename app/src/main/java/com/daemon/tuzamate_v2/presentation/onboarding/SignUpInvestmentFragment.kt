package com.daemon.tuzamate_v2.presentation.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.daemon.tuzamate_v2.MainActivity
import com.daemon.tuzamate_v2.databinding.FragmentSignUpInvestmentBinding
import androidx.navigation.findNavController
import com.daemon.tuzamate_v2.R
import com.daemon.tuzamate_v2.utils.animateProgress

class SignUpInvestmentFragment : Fragment() {

    private lateinit var navController: NavController
    private var _binding: FragmentSignUpInvestmentBinding? = null
    private val binding: FragmentSignUpInvestmentBinding
        get() = requireNotNull(_binding){"FragmentSignUpInvestmentBinding -> null"}

    // 현재 선택된 버튼을 추적
    private var selectedButton: View? = null
    private var selectedInvestmentLevel: InvestmentLevel? = null

    // 투자 레벨 enum
    enum class InvestmentLevel {
        NO_INVESTMENT,
        DOING_INVESTMENT,
        GOOD_AT_INVESTMENT
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentSignUpInvestmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        (requireActivity() as MainActivity).hideBottomNavigation(true)

        binding.progressBar.animateProgress(100)

        setupClickListeners()
        setupInitialState()

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun setupClickListeners() {
        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // 투자 선택 버튼들
        binding.btnInvestmentNo.setOnClickListener {
            selectInvestmentLevel(binding.btnInvestmentNo, InvestmentLevel.NO_INVESTMENT)
        }

        binding.btnInvestmentYes.setOnClickListener {
            selectInvestmentLevel(binding.btnInvestmentYes, InvestmentLevel.DOING_INVESTMENT)
        }

        binding.btnInvestmentGood.setOnClickListener {
            selectInvestmentLevel(binding.btnInvestmentGood, InvestmentLevel.GOOD_AT_INVESTMENT)
        }

        // 확인 버튼
        binding.btnConfirm.setOnClickListener {
            if (selectedInvestmentLevel != null) {
                navController.navigate(R.id.action_navigation_sign_up_investment_to_ai)
            }
        }
    }

    private fun setupInitialState() {
        // 초기 상태에서 확인 버튼 비활성화
        updateConfirmButton(false)
    }

    private fun selectInvestmentLevel(clickedButton: View, level: InvestmentLevel) {
        // 이전 선택 해제
        selectedButton?.let { resetButtonState(it) }

        // 새로운 선택 적용
        setSelectedButtonState(clickedButton)
        selectedButton = clickedButton
        selectedInvestmentLevel = level

        // 확인 버튼 활성화
        updateConfirmButton(true)
    }

    private fun setSelectedButtonState(button: View) {
        button.isSelected = true
        if (button is TextView) {
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.W300))
        }
    }

    private fun resetButtonState(button: View) {
        button.isSelected = false
        if (button is TextView) {
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.T300))
        }
    }

    private fun updateConfirmButton(isEnabled: Boolean) {
        binding.btnConfirm.isEnabled = isEnabled
        binding.btnConfirm.isSelected = isEnabled

        val textColor = if (isEnabled) {
            ContextCompat.getColor(requireContext(), R.color.W300)
        } else {
            ContextCompat.getColor(requireContext(), R.color.T75)
        }
        binding.btnConfirm.setTextColor(textColor)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}