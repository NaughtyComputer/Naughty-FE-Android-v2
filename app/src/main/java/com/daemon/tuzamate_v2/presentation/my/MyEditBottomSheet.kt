package com.daemon.tuzamate_v2.presentation.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daemon.tuzamate_v2.R
import com.daemon.tuzamate_v2.databinding.BottomSheetMyEditBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyEditBottomSheet(
    private val onImageSelected: (Int) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetMyEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetMyEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.creditRedBox.setOnClickListener {
            onImageSelected(R.drawable.ic_credit_red)
            dismiss()
        }
        binding.creditBlueBox.setOnClickListener {
            onImageSelected(R.drawable.ic_credit_blue)
            dismiss()
        }
        binding.creditBlackBox.setOnClickListener {
            onImageSelected(R.drawable.ic_credit_black)
            dismiss()
        }
        binding.creditGreenBox.setOnClickListener {
            onImageSelected(R.drawable.ic_credit_green)
            dismiss()
        }
        binding.creditYellowBox.setOnClickListener {
            onImageSelected(R.drawable.ic_credit_yellow)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
