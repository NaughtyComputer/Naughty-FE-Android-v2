package com.daemon.tuzamate_v2.presentation.onboarding

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
import com.daemon.tuzamate_v2.MainActivity
import com.daemon.tuzamate_v2.R
import com.daemon.tuzamate_v2.databinding.FragmentSignUpNicknameBinding
import androidx.navigation.findNavController
import com.daemon.tuzamate_v2.utils.animateProgress

class SignUpNicknameFragment : Fragment() {

    private lateinit var navController: NavController
    private var _binding: FragmentSignUpNicknameBinding? = null
    private val binding: FragmentSignUpNicknameBinding
        get() = requireNotNull(_binding){"FragmentSignUpNicknameBinding -> null"}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentSignUpNicknameBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        (requireActivity() as MainActivity).hideBottomNavigation(true)

        binding.progressBar.animateProgress(50)

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.etSignUpNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 별도 처리 없음
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 사용자가 입력 중일 때 배경을 입력 중 상태 drawable로 변경
                binding.etSignUpNickname.setBackgroundResource(R.drawable.bg_et_onboarding_active)
            }

            override fun afterTextChanged(s: Editable?) {
                val nickname = s.toString().trim()
                if (nickname.isEmpty()) {
                    // 입력이 없으면 기본 배경, 오류 메시지 감추기, 버튼 비활성화
                    binding.etSignUpNickname.setBackgroundResource(R.drawable.bg_et_onboarding_default)
//                    binding.tvError.visibility = View.GONE
//                    binding.tvDefault.visibility = View.VISIBLE
                    binding.btnConfirm.isEnabled = false
                    binding.btnConfirm.isSelected = false
                } else if (isValidEmail(nickname)) {
                    binding.etSignUpNickname.setBackgroundResource(R.drawable.bg_et_onboarding_active)
//                    binding.tvError.visibility = View.GONE
//                    binding.tvDefault.visibility = View.VISIBLE
                    binding.btnConfirm.isEnabled = true
                    binding.btnConfirm.isSelected = true
                    binding.btnConfirm.setTextColor(ContextCompat.getColor(requireContext(), R.color.W300))
                } else {
                    binding.etSignUpNickname.setBackgroundResource(R.drawable.bg_et_onboarding_error)
//                    binding.tvError.visibility = View.VISIBLE
//                    binding.tvDefault.visibility = View.GONE
                    binding.btnConfirm.isEnabled = false
                    binding.btnConfirm.isSelected = false
                }
            }
        })

        binding.btnConfirm.setOnClickListener {
            hideKeyboard(binding.etSignUpNickname)
            navController.navigate(R.id.action_navigation_sign_up_nickname_to_investment)
        }

//        viewModel.signUpResult.observe(viewLifecycleOwner) { result ->
//            result.fold(
//                onSuccess = {
//                    navController.navigate(R.id.action_navigation_sign_up_nickname_to_onboarding)
//                },
//                onFailure = { error ->
//                    Toast.makeText(context, "회원가입 실패: ${error.message}", Toast.LENGTH_SHORT).show()
//                }
//            )
//        }
    }

    private fun hideKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun isValidEmail(nickname: String): Boolean {
        // 로컬 파트에 해당하는 문자열이 한글 또는 영문으로 1~5자 구성되었는지 검사
        val regex = "^[A-Za-z가-힣]{1,5}$"
        return Regex(regex).matches(nickname)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}