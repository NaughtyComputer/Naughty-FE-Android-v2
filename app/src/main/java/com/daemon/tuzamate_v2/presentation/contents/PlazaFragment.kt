package com.daemon.tuzamate_v2.presentation.contents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.daemon.tuzamate_v2.R
import com.daemon.tuzamate_v2.databinding.FragmentPlazaBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlazaFragment : Fragment() {

    private var _binding: FragmentPlazaBinding? = null
    private val binding: FragmentPlazaBinding
        get() = requireNotNull(_binding){"FragmentPlazaBinding -> null"}

    private lateinit var plazaAdapter: PlazaAdapter
    private val plazaViewModel: PlazaViewModel by viewModels()

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
        setupFab()
        observeViewModel()
        setupNavigationResultListener()
    }
    
    override fun onResume() {
        super.onResume()
        // Fragment가 다시 표시될 때마다 게시글 목록 새로고침
        plazaViewModel.loadPosts(isRefresh = true)
    }
    
    private fun setupNavigationResultListener() {
        // 게시글 작성 화면에서 돌아왔을 때 결과를 받아 처리
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("post_created")
            ?.observe(viewLifecycleOwner) { postCreated ->
                if (postCreated) {
                    // 게시글 목록 새로고침
                    plazaViewModel.loadPosts(isRefresh = true)
                    // 플래그 제거
                    findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>("post_created")
                }
            }
    }

    private fun setupRecyclerView() {
        plazaAdapter = PlazaAdapter(
            onItemClick = { post ->
                val bundle = bundleOf(
                    "postId" to post.id,
                    "boardType" to "FREE"
                )
                findNavController().navigate(R.id.navigation_article, bundle)
            },
            onLikeClick = { post ->
                plazaViewModel.toggleLike(post.id)
            }
        )

        binding.rvPlaza.apply {
            adapter = plazaAdapter
            layoutManager = LinearLayoutManager(requireContext())

            val divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            addItemDecoration(divider)
        }
    }
    
    private fun setupFab() {
        binding.fabCreatePost.setOnClickListener {
            findNavController().navigate(R.id.navigation_contents_create)
        }
    }

    private fun observeViewModel() {
        plazaViewModel.posts.observe(viewLifecycleOwner) { posts ->
            plazaAdapter.submitList(posts) {
                // 새 게시글이 추가되었을 때 최상단으로 스크롤
                // submitList가 완료된 후 실행
                if (posts.isNotEmpty()) {
                    binding.rvPlaza.scrollToPosition(0)
                }
            }
        }
        
        plazaViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
        }
        
        plazaViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
        
        plazaViewModel.hasNext.observe(viewLifecycleOwner) { hasNext ->
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}