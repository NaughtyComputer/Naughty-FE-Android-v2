package com.daemon.tuzamate_v2.presentation.contents

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.daemon.tuzamate_v2.R
import com.daemon.tuzamate_v2.databinding.FragmentContentsCreateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentsCreateFragment : Fragment() {

    private var _binding: FragmentContentsCreateBinding? = null
    private val binding: FragmentContentsCreateBinding
        get() = requireNotNull(_binding) { "FragmentContentsCreateBinding -> null" }

    private val contentsCreateViewModel: ContentsCreateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentsCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.etTitle.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // 제목에 포커스가 있을 때는 active 배경
                binding.etTitle.setBackgroundResource(R.drawable.bg_et_onboarding_active)
                // 내용 EditText는 default 배경으로 변경
                binding.etContents.setBackgroundResource(R.drawable.bg_et_onboarding_default)
            } else {
                // 제목이 비어있으면 default 배경
                if (binding.etTitle.text.isNullOrEmpty()) {
                    binding.etTitle.setBackgroundResource(R.drawable.bg_et_onboarding_default)
                } else {
                    // 텍스트가 있으면 default 배경 유지
                    binding.etTitle.setBackgroundResource(R.drawable.bg_et_onboarding_default)
                }
            }
        }

        binding.etContents.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // 내용에 포커스가 있을 때는 active 배경
                binding.etContents.setBackgroundResource(R.drawable.bg_et_onboarding_active)
                // 제목 EditText는 default 배경으로 변경
                binding.etTitle.setBackgroundResource(R.drawable.bg_et_onboarding_default)
            } else {
                // 내용이 비어있으면 default 배경
                if (binding.etContents.text.isNullOrEmpty()) {
                    binding.etContents.setBackgroundResource(R.drawable.bg_et_onboarding_default)
                } else {
                    // 텍스트가 있으면 default 배경 유지
                    binding.etContents.setBackgroundResource(R.drawable.bg_et_onboarding_default)
                }
            }
        }

        binding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Focus가 있을 때만 active 배경 적용
                if (binding.etTitle.hasFocus() && s?.isNotEmpty() == true) {
                    binding.etTitle.setBackgroundResource(R.drawable.bg_et_onboarding_active)
                }
            }
            
            override fun afterTextChanged(s: Editable?) {
                updateConfirmButtonState()
            }
        })

        binding.etContents.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Focus가 있을 때만 active 배경 적용
                if (binding.etContents.hasFocus() && s?.isNotEmpty() == true) {
                    binding.etContents.setBackgroundResource(R.drawable.bg_et_onboarding_active)
                }
            }
            
            override fun afterTextChanged(s: Editable?) {
                updateConfirmButtonState()
            }
        })

        binding.btnConfirm.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContents.text.toString()
            
            if (title.isNotBlank() && content.isNotBlank()) {
                contentsCreateViewModel.createPost(title, content)
            }
        }
        
        updateConfirmButtonState()
    }

    private fun updateConfirmButtonState() {
        val title = binding.etTitle.text?.toString() ?: ""
        val content = binding.etContents.text?.toString() ?: ""
        
        val isEnabled = title.isNotBlank() && content.isNotBlank()
        binding.btnConfirm.isEnabled = isEnabled
        binding.btnConfirm.isSelected = isEnabled
        
        if (isEnabled) {
            binding.btnConfirm.setTextColor(ContextCompat.getColor(requireContext(), R.color.W300))
        } else {
            binding.btnConfirm.setTextColor(ContextCompat.getColor(requireContext(), R.color.T75))
        }
    }

    private fun observeViewModel() {
        contentsCreateViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnConfirm.isEnabled = !isLoading && 
                binding.etTitle.text?.isNotBlank() == true && 
                binding.etContents.text?.isNotBlank() == true
        }

        contentsCreateViewModel.createSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "게시물이 작성되었습니다.", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        contentsCreateViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}