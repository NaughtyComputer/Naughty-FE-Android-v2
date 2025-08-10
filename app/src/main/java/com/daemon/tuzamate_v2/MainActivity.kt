package com.daemon.tuzamate_v2

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.daemon.tuzamate_v2.data.model.BoardType
import com.daemon.tuzamate_v2.data.repository.PostRepository
import com.daemon.tuzamate_v2.data.repository.ProfileRepository
import com.daemon.tuzamate_v2.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    
    @Inject
    lateinit var profileRepository: ProfileRepository
    
    @Inject
    lateinit var postRepository: PostRepository
    
    companion object {
        private const val TAG = "API_TEST"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // API 테스트 실행
        testApis()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

        val navController = navHostFragment.navController

        // BottomNavigationView 설정
        binding.bottomNavi.setupWithNavController(navController)

        // BottomNavigationView 아이템 선택 리스너 설정
        binding.bottomNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_contents -> {
                    navController.navigate(R.id.navigation_contents)
                    true
                }
                R.id.navigation_ai -> {
                    navController.navigate(R.id.navigation_ai)
                    true
                }
                R.id.navigation_my -> {
                    navController.navigate(R.id.navigation_my)
                    true
                }
                else -> false
            }
        }
    }

    private fun testApis() {
        // 1. 프로필 조회 API 테스트
        lifecycleScope.launch {
            try {
                Log.d(TAG, "========== 프로필 조회 API 테스트 시작 ==========")
                val profileResponse = profileRepository.getProfile()
                
                if (profileResponse.isSuccessful) {
                    val profile = profileResponse.body()
                    Log.d(TAG, "프로필 조회 성공!")
                    Log.d(TAG, "Success: ${profile?.isSuccess}")
                    Log.d(TAG, "Status: ${profile?.status}")
                    Log.d(TAG, "Message: ${profile?.message}")
                    Log.d(TAG, "Email: ${profile?.result?.myInfo?.email}")
                    Log.d(TAG, "Nickname: ${profile?.result?.myInfo?.nickname}")
                    Log.d(TAG, "Gender: ${profile?.result?.myInfo?.gender}")
                    Log.d(TAG, "Income: ${profile?.result?.investmentInfo?.income}")
                    Log.d(TAG, "Type: ${profile?.result?.investmentInfo?.type}")
                    Log.d(TAG, "Purpose: ${profile?.result?.investmentInfo?.purpose}")
                } else {
                    Log.e(TAG, "프로필 조회 실패: ${profileResponse.code()} - ${profileResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "프로필 조회 예외 발생", e)
            }
        }
        
        // 2. 게시글 작성 API 테스트
        lifecycleScope.launch {
            try {
                Log.d(TAG, "========== 게시글 작성 API 테스트 시작 ==========")
                val postResponse = postRepository.createPost(
                    boardType = BoardType.FREE,
                    title = "안드로이드 테스트 게시글",
                    content = "API 테스트 중입니다."
                )
                
                if (postResponse.isSuccessful) {
                    val post = postResponse.body()
                    Log.d(TAG, "게시글 작성 성공!")
                    Log.d(TAG, "Success: ${post?.isSuccess}")
                    Log.d(TAG, "Status: ${post?.status}")
                    Log.d(TAG, "Message: ${post?.message}")
                    Log.d(TAG, "Post ID: ${post?.result?.id}")
                    Log.d(TAG, "Created At: ${post?.result?.createdAt}")
                } else {
                    Log.e(TAG, "게시글 작성 실패: ${postResponse.code()} - ${postResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "게시글 작성 예외 발생", e)
            }
        }
    }
    
    fun hideBottomNavigation(state:Boolean){
        if(state) binding.bottomNavi.visibility = View.GONE else binding.bottomNavi.visibility=
            View.VISIBLE
    }
}