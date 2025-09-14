package com.daemon.tuzamate_v2.presentation.ai

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.daemon.tuzamate_v2.MainActivity
import com.daemon.tuzamate_v2.R
import com.daemon.tuzamate_v2.data.model.ChatMessage
import com.daemon.tuzamate_v2.data.repository.ChatRepository
import com.daemon.tuzamate_v2.databinding.FragmentAiBinding
import com.daemon.tuzamate_v2.utils.startGlowing
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AIFragment : Fragment() {

    @Inject
    lateinit var chatRepository: ChatRepository

    private lateinit var navController: NavController
    private var _binding: FragmentAiBinding? = null
    private val binding: FragmentAiBinding
        get() = requireNotNull(_binding){"FragmentAiBinding -> null"}
    
    private lateinit var chatAdapter: ChatAdapter
    private val chatMessages = mutableListOf<ChatMessage>()
    private var currentSessionId: String? = null
    private val fixedUsername = "tristan1006@naver.com"

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
        
        setupRecyclerView()

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
            val message = binding.etAI.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
            }
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.rvChatMessages.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
        }
    }
    
    private fun sendMessage(message: String) {
        hideKeyboard(binding.etAI)
        binding.etAI.setBackgroundResource(R.drawable.bg_et_ai_default)
        binding.btnSend.isEnabled = false
        binding.btnSend.isSelected = false
        
        // Hide skeleton UI and show chat messages
        binding.scrollSkeletonUI.visibility = View.GONE
        binding.rvChatMessages.visibility = View.VISIBLE
        
        // Add user message to chat
        val userMessage = ChatMessage(message, true)
        chatMessages.add(userMessage)
        chatAdapter.submitList(chatMessages.toList())
        
        // Clear input
        binding.etAI.setText("")
        
        // Send message to API
        lifecycleScope.launch {
            try {
                // Send chat message
                val chatResponse = chatRepository.sendChatMessage(
                    username = fixedUsername,
                    sessionId = currentSessionId,
                    message = message
                )
                
                if (chatResponse.isSuccessful && chatResponse.body()?.isSuccess == true) {
                    val result = chatResponse.body()?.result
                    result?.let {
                        currentSessionId = it.sessionId
                        
                        // Check if response is immediate or requires polling
                        if (!it.response.isNullOrEmpty()) {
                            // Direct response available
                            val aiMessage = ChatMessage(it.response, false)
                            chatMessages.add(aiMessage)
                            chatAdapter.submitList(chatMessages.toList())
                            scrollToBottom()
                            
                            // Enable send button
                            binding.btnSend.isEnabled = true
                            binding.btnSend.isSelected = true
                        } else if (!it.taskId.isNullOrEmpty()) {
                            // Need to poll for task completion
                            // Add loading message
                            val loadingMessage = ChatMessage("투자 메이트가 생각중이에요...", false)
                            chatMessages.add(loadingMessage)
                            chatAdapter.submitList(chatMessages.toList())
                            scrollToBottom()
                            
                            // Poll for task completion
                            pollTaskStatus(it.taskId)
                        } else {
                            showError("응답을 받을 수 없습니다.")
                            binding.btnSend.isEnabled = true
                            binding.btnSend.isSelected = true
                        }
                    }
                } else {
                    showError("메시지 전송에 실패했습니다.")
                    binding.btnSend.isEnabled = true
                    binding.btnSend.isSelected = true
                }
            } catch (e: Exception) {
                showError("네트워크 오류가 발생했습니다.")
                binding.btnSend.isEnabled = true
                binding.btnSend.isSelected = true
            }
        }
    }
    
    private suspend fun pollTaskStatus(taskId: String) {
        var retryCount = 0
        val maxRetries = 30 // 30 * 2초 = 60초 최대 대기
        
        while (retryCount < maxRetries) {
            delay(2000) // 2초 대기
            
            try {
                val taskResponse = chatRepository.getTaskStatus(taskId)
                
                if (taskResponse.isSuccessful && taskResponse.body()?.isSuccess == true) {
                    val taskResult = taskResponse.body()?.result
                    
                    if (taskResult?.status == "completed") {
                        // Remove loading message
                        chatMessages.removeAt(chatMessages.size - 1)
                        
                        // Add AI response
                        val aiResponse = taskResult.result?.response ?: "응답을 받을 수 없습니다."
                        val aiMessage = ChatMessage(aiResponse, false)
                        chatMessages.add(aiMessage)
                        chatAdapter.submitList(chatMessages.toList())
                        scrollToBottom()
                        
                        // Enable send button
                        binding.btnSend.isEnabled = true
                        binding.btnSend.isSelected = true
                        return
                    }
                }
            } catch (e: Exception) {
                // Continue polling
            }
            
            retryCount++
        }
        
        // Timeout - remove loading message and show error
        chatMessages.removeAt(chatMessages.size - 1)
        val errorMessage = ChatMessage("응답 시간이 초과되었습니다. 다시 시도해주세요.", false)
        chatMessages.add(errorMessage)
        chatAdapter.submitList(chatMessages.toList())
        scrollToBottom()
        
        binding.btnSend.isEnabled = true
        binding.btnSend.isSelected = true
    }
    
    private fun scrollToBottom() {
        binding.rvChatMessages.scrollToPosition(chatMessages.size - 1)
    }
    
    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    
    private fun hideKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // End chat session if exists
        currentSessionId?.let { sessionId ->
            lifecycleScope.launch {
                try {
                    chatRepository.endChatSession(sessionId)
                } catch (e: Exception) {
                    // Ignore errors on destroy
                }
            }
        }
        _binding = null
    }
}