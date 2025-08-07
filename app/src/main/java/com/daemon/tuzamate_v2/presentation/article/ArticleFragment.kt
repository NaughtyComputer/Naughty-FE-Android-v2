package com.daemon.tuzamate_v2.presentation.article

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.daemon.tuzamate_v2.MainActivity
import com.daemon.tuzamate_v2.R
import com.daemon.tuzamate_v2.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {

    private lateinit var navController: NavController
    private var _binding: FragmentArticleBinding? = null
    private val binding: FragmentArticleBinding
        get() = requireNotNull(_binding){"FragmentArticleBinding -> null"}

    private var progressTimer: CountDownTimer? = null
    private var isScreenActive = true
    private var isLiked = false
    private var isBookmarked = false

    companion object {
        private const val TOTAL_TIME_MS = 10000L
        private const val TICK_INTERVAL_MS = 100L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentArticleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        (requireActivity() as MainActivity).hideBottomNavigation(true)

        setupClickListeners()
        startProgressTimer()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
        
        binding.btnLike.setOnClickListener {
            toggleLike()
        }
        
        binding.btnBookmark.setOnClickListener {
            toggleBookmark()
        }
    }
    
    private fun toggleLike() {
        isLiked = !isLiked
        if (isLiked) {
            binding.icLike.setImageResource(R.drawable.ic_like_filled)
        } else {
            binding.icLike.setImageResource(R.drawable.ic_like_empty)
        }
    }
    
    private fun toggleBookmark() {
        isBookmarked = !isBookmarked
        if (isBookmarked) {
            binding.btnBookmark.setImageResource(R.drawable.ic_bookmark_filled)
        } else {
            binding.btnBookmark.setImageResource(R.drawable.ic_bookmark_empty)
        }
    }

    private fun startProgressTimer() {
        progressTimer = object : CountDownTimer(TOTAL_TIME_MS, TICK_INTERVAL_MS) {
            override fun onTick(millisUntilFinished: Long) {
                if (isScreenActive) {
                    val elapsedTime = TOTAL_TIME_MS - millisUntilFinished
                    val progress = ((elapsedTime.toFloat() / TOTAL_TIME_MS) * 100).toInt()

                    binding.progressBar.progress = progress

                    val remainingSeconds = (millisUntilFinished / 1000) + 1
                     binding.timerText.text = "${remainingSeconds}초" // 필요시 활용
                }
            }

            override fun onFinish() {
                if (isScreenActive) {
                    binding.progressBar.progress = 100
                    showCompletionToast()
                }
            }
        }.start()
    }

    private fun showCompletionToast() {
        Toast.makeText(this.requireContext(), "리워드 획득!", Toast.LENGTH_LONG).show()
        resetUIToInitialState()
    }

    override fun onResume() {
        super.onResume()
        isScreenActive = true
        if (progressTimer == null) {
            startProgressTimer()
        }
    }

    override fun onPause() {
        super.onPause()
        isScreenActive = false
        resetTimer()
    }

    private fun resetTimer() {
        progressTimer?.cancel()
        binding.progressBar.progress = 0
        resetUIToInitialState()
    }

    private fun resetUIToInitialState() {
        binding.timerText.text = "10크레딧"
        binding.timerText.setTextColor(ContextCompat.getColor(requireContext(), R.color.Y300))
        binding.icTimer.setImageResource(R.drawable.ic_credit_yellow)
        binding.progressBar.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Y300))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        progressTimer?.cancel()
    }
}