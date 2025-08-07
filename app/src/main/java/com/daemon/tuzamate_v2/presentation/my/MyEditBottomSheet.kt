package com.daemon.tuzamate_v2.presentation.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daemon.tuzamate_v2.databinding.BottomSheetMyEditBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyEditBottomSheet(
    private val onImageSelected: (Int) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetMyEditBinding? = null
    private val binding get() = _binding!!

    private var selectedImageRes: Int? = null

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
            selectedImageRes?.let { onImageSelected(it) }
            dismiss()
        }
        binding.creditBlueBox.setOnClickListener {
            selectedImageRes?.let { onImageSelected(it) }
            dismiss()
        }
        binding.creditBlackBox.setOnClickListener {
            selectedImageRes?.let { onImageSelected(it) }
            dismiss()
        }
        binding.creditGreenBox.setOnClickListener {
            selectedImageRes?.let { onImageSelected(it) }
            dismiss()
        }
        binding.creditYellowBox.setOnClickListener {
            selectedImageRes?.let { onImageSelected(it) }
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
