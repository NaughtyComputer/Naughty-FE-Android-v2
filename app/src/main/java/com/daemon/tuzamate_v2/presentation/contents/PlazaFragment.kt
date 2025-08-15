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
        observeViewModel()
        plazaViewModel.loadPosts(isRefresh = true)
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

    private fun observeViewModel() {
        plazaViewModel.posts.observe(viewLifecycleOwner) { posts ->
            plazaAdapter.submitList(posts)
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