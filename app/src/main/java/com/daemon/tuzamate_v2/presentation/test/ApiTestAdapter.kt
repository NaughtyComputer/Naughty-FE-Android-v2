package com.daemon.tuzamate_v2.presentation.test

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daemon.tuzamate_v2.R
import com.daemon.tuzamate_v2.databinding.ItemApiTestResultBinding

enum class TestStatus {
    SUCCESS, FAILURE, ERROR
}

data class ApiTestResult(
    val testName: String,
    val status: TestStatus,
    val message: String,
    val responseCode: Int
)

class ApiTestAdapter(
    private val results: List<ApiTestResult>
) : RecyclerView.Adapter<ApiTestAdapter.TestResultViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestResultViewHolder {
        val binding = ItemApiTestResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TestResultViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: TestResultViewHolder, position: Int) {
        holder.bind(results[position])
    }
    
    override fun getItemCount() = results.size
    
    class TestResultViewHolder(
        private val binding: ItemApiTestResultBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(result: ApiTestResult) {
            binding.apply {
                tvTestName.text = result.testName
                tvResponseCode.text = "Code: ${result.responseCode}"
                tvMessage.text = result.message
                
                val (statusText, statusColor) = when (result.status) {
                    TestStatus.SUCCESS -> "✓ SUCCESS" to R.color.success_green
                    TestStatus.FAILURE -> "✗ FAILURE" to R.color.error_red
                    TestStatus.ERROR -> "⚠ ERROR" to R.color.warning_orange
                }
                
                tvStatus.text = statusText
                tvStatus.setTextColor(
                    ContextCompat.getColor(itemView.context, statusColor)
                )
                
                cardContainer.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        when (result.status) {
                            TestStatus.SUCCESS -> R.color.success_bg
                            TestStatus.FAILURE -> R.color.error_bg
                            TestStatus.ERROR -> R.color.warning_bg
                        }
                    )
                )
            }
        }
    }
}