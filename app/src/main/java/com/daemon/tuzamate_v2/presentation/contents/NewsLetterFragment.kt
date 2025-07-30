package com.daemon.tuzamate_v2.presentation.contents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.daemon.tuzamate_v2.databinding.FragmentNewsLetterBinding

class NewsLetterFragment : Fragment() {

    private var _binding: FragmentNewsLetterBinding? = null
    private val binding: FragmentNewsLetterBinding
        get() = requireNotNull(_binding){"FragmentNewsLetterBinding -> null"}

    private lateinit var newsLetterAdapter: NewsLetterAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentNewsLetterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        newsLetterAdapter = NewsLetterAdapter { newsLetter ->
            // 아이템 클릭 처리
            onNewsLetterItemClick(newsLetter)
        }

        binding.rvNewsLetter.apply {
            adapter = newsLetterAdapter
            layoutManager = LinearLayoutManager(requireContext())

            // 구분선 추가 (선택사항)
            val divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            addItemDecoration(divider)
        }
    }

    private fun onNewsLetterItemClick(newsLetter: NewsLetter) {
        // 뉴스레터 상세 화면으로 이동하거나 웹뷰 열기
        // 예:
        // findNavController().navigate(
        //     R.id.action_newsletter_to_detail,
        //     bundleOf("newsletterId" to newsLetter.id)
        // )
    }

    private fun loadSampleData() {
        // 샘플 데이터 로드 (실제로는 ViewModel에서 가져와야 함)
        val sampleData = listOf(
            NewsLetter(
                id = 1,
                title = "AI 기술의 최신 동향과 미래 전망",
                content = "인공지능 기술이 빠르게 발전하면서 우리 생활의 모든 영역에 큰 변화를 가져오고 있습니다.",
                author = "김개발",
                publishDate = "2024.01.15",
                readTime = "5분 읽기",
                category = "테크",
                imageUrl = null,
                isRead = false
            ),
            NewsLetter(
                id = 2,
                title = "2024년 스타트업 투자 트렌드",
                content = "올해 벤처캐피털들이 주목하고 있는 분야와 투자 패턴을 분석해보겠습니다.",
                author = "박투자",
                publishDate = "2024.01.14",
                readTime = "7분 읽기",
                category = "비즈니스",
                imageUrl = null,
                isRead = true
            ),
            NewsLetter(
                id = 3,
                title = "클린테크 산업의 성장 가능성",
                content = "환경 문제 해결과 함께 새로운 비즈니스 기회를 창출하는 클린테크 기업들을 살펴봅니다.",
                author = "이환경",
                publishDate = "2024.01.13",
                readTime = "6분 읽기",
                category = "환경",
                imageUrl = null,
                isRead = false
            )
        )

        newsLetterAdapter.submitList(sampleData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}