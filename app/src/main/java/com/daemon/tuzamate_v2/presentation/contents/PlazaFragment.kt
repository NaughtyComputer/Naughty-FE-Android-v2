package com.daemon.tuzamate_v2.presentation.contents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.daemon.tuzamate_v2.databinding.FragmentPlazaBinding

class PlazaFragment : Fragment() {

    private var _binding: FragmentPlazaBinding? = null
    private val binding: FragmentPlazaBinding
        get() = requireNotNull(_binding){"FragmentPlazaBinding -> null"}

    private lateinit var newsLetterAdapter: NewsLetterAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentPlazaBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadSampleData()
    }

    private fun setupRecyclerView() {
        newsLetterAdapter = NewsLetterAdapter { newsLetter ->
            // 아이템 클릭 처리
            onNewsLetterItemClick(newsLetter)
        }

        binding.rvPlaza.apply {
            adapter = newsLetterAdapter
            layoutManager = LinearLayoutManager(requireContext())

            val divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            addItemDecoration(divider)
        }
    }

    private fun onNewsLetterItemClick(newsLetter: NewsLetter) {
    }

    private fun loadSampleData() {
        // 샘플 데이터 로드 (실제로는 ViewModel에서 가져와야 함)
        val sampleData = listOf(
            NewsLetter(
                id = 1
            ),
            NewsLetter(
                id = 2
            ),
            NewsLetter(
                id = 3
            )
        )

        newsLetterAdapter.submitList(sampleData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}