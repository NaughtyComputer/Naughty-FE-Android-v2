package com.daemon.tuzamate_v2.utils

import android.R.style
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.daemon.tuzamate_v2.R
import com.daemon.tuzamate_v2.databinding.LayoutTabBarBinding

class CustomTabBar(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    private var binding: LayoutTabBarBinding =
        LayoutTabBarBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var listTabName: List<String>
    private lateinit var listTabTv: List<TextView>
    private var currentSelectedIndex = 0
    private var onTabSelectedListener: ((Int) -> Unit)? = null

    private var tabTextSize: Float = 16f
    private var tabFontFamily: Typeface? = null

    init {
        setupAttrs(attrs)
        setupUI()
    }

    private fun setupAttrs(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.CustomTabBar,
            0, 0
        )

        listTabName = typedArray
            .getTextArray(R.styleable.CustomTabBar_android_entries)
            ?.toList()?.map { it.toString() } ?: emptyList()

        // 텍스트 사이즈 설정 (기본값: 12sp)
        tabTextSize = typedArray.getDimension(R.styleable.CustomTabBar_tabTextSize, 12f * resources.displayMetrics.scaledDensity) / resources.displayMetrics.scaledDensity

        // 폰트 패밀리 설정
        val fontResId = typedArray.getResourceId(R.styleable.CustomTabBar_tabFontFamily, -1)
        if (fontResId != -1) {
            tabFontFamily = ResourcesCompat.getFont(context, fontResId)
        }

        typedArray.recycle()
    }

    private fun setupUI() {
        // TextView 생성
        listTabTv = listTabName.mapIndexed { index, tabName ->
            initTabTv(tabName, index)
        }

        // view_tabs_wrapper 설정
        binding.viewTabsWrapper.apply {
            weightSum = listTabTv.size.toFloat()
            listTabTv.forEach { textView ->
                this.addView(textView)
            }
        }

        // view_indicator_wrapper 설정
        binding.viewIndicatorWrapper.apply {
            weightSum = listTabTv.size.toFloat()
        }

        // 첫 번째 탭을 기본 선택으로 설정
        if (listTabTv.isNotEmpty()) {
            updateTabSelection(0)
        }
    }

    private fun initTabTv(tabName: String, index: Int) = TextView(context).apply {
        text = tabName
        layoutParams = LinearLayout.LayoutParams(
            0,
            LayoutParams.MATCH_PARENT,
            1f // weight of each tab = 1
        )
        gravity = Gravity.CENTER
        setTextColor(ContextCompat.getColor(this.context, R.color.T300))
        textSize = tabTextSize
        tabFontFamily?.let { typeface = it }
        setPadding(16, 8, 16, 8)

        setOnClickListener {
            selectTab(index)
        }
    }

    private fun selectTab(index: Int) {
        if (index == currentSelectedIndex) return

        onTabSelected(index)
        updateTabSelection(index)
        currentSelectedIndex = index
        onTabSelectedListener?.invoke(index)
    }

    private fun onTabSelected(index: Int) {
        ObjectAnimator.ofFloat(
            binding.viewIndicator,
            View.TRANSLATION_X,
            binding.viewIndicator.translationX,
            (width / listTabTv.size) * index.toFloat()
        ).apply {
            duration = 300
            start()
        }
    }

    private fun updateTabSelection(selectedIndex: Int) {
        listTabTv.forEachIndexed { index, textView ->
            if (index == selectedIndex) {
                textView.setTextColor(ContextCompat.getColor(context, R.color.T300))
                textView.alpha = 1.0f
            } else {
                textView.setTextColor(ContextCompat.getColor(context, R.color.T300))
                textView.alpha = 0.7f
            }
        }
    }

    // 외부에서 탭 선택을 위한 메서드
    fun setSelectedTab(index: Int) {
        if (index in 0 until listTabTv.size) {
            selectTab(index)
        }
    }

    // 현재 선택된 탭 인덱스 반환
    fun getCurrentSelectedIndex(): Int = currentSelectedIndex

    // 탭 선택 리스너 설정
    fun setOnTabSelectedListener(listener: (Int) -> Unit) {
        onTabSelectedListener = listener
    }

    // ViewPager2와 연동을 위한 메서드
    fun updateIndicatorPosition(position: Int, positionOffset: Float) {
        val targetX = (width / listTabTv.size) * (position + positionOffset)
        binding.viewIndicator.translationX = targetX
    }
}