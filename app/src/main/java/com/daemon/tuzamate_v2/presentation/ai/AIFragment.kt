package com.daemon.tuzamate_v2.presentation.ai

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.daemon.tuzamate_v2.MainActivity
import com.daemon.tuzamate_v2.R
import com.daemon.tuzamate_v2.databinding.FragmentAiBinding
import com.daemon.tuzamate_v2.utils.startGlowing

class AIFragment : Fragment() {

    private lateinit var navController: NavController
    private var _binding: FragmentAiBinding? = null
    private val binding: FragmentAiBinding
        get() = requireNotNull(_binding){"FragmentAiBinding -> null"}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentAiBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        (requireActivity() as MainActivity).hideBottomNavigation(false)

        binding.etAI.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 별도 처리 없음
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 사용자가 입력 중일 때 배경을 입력 중 상태 drawable로 변경
                binding.etAI.setBackgroundResource(R.drawable.bg_et_ai_active)
            }

            override fun afterTextChanged(s: Editable?) {
                val nickname = s.toString().trim()
                if (nickname.isEmpty()) {
                    // 입력이 없으면 기본 배경, 오류 메시지 감추기, 버튼 비활성화
                    binding.etAI.setBackgroundResource(R.drawable.bg_et_ai_default)
                    binding.btnSend.isEnabled = false
                    binding.btnSend.isSelected = false
                } else {
                    binding.etAI.setBackgroundResource(R.drawable.bg_et_ai_active)
                    binding.btnSend.isEnabled = true
                    binding.btnSend.isSelected = true
                }
            }
        })

        binding.btnSend.setOnClickListener {
            hideKeyboard(binding.etAI)
            binding.etAI.setBackgroundResource(R.drawable.bg_et_ai_default)
            binding.btnSend.isEnabled = false
            binding.btnSend.isSelected = false
            binding.scrollSkeletonUI.visibility = View.INVISIBLE
            binding.scrollContent.visibility = View.VISIBLE
            binding.answer.startGlowing(
                duration = 1000,
                repeatCount = 10,
                glowColor = ContextCompat.getColor(requireContext(), R.color.T100)
            )
            binding.etAI.setText("")
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}