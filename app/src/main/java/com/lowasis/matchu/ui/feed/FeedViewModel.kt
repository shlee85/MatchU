package com.lowasis.matchu.ui.feed

import android.util.Log
import com.lowasis.matchu.R
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//게시글 모델 클래스
data class FeedItem (
    val imageResId: Int,
    val text: String
)

class FeedViewModel : ViewModel(){
    //LiveData로 게시글 리스트 관리
    private val mFeedItems = MutableLiveData<List<FeedItem>>()
    val feedItems: LiveData<List<FeedItem>> = mFeedItems

    init {
        Log.i(TAG, "init!!!")
        //초기 더미 데이터 설정
        mFeedItems.value = listOf(
            FeedItem(R.drawable.sample1, "주말에 공원갈까요?"),
            FeedItem(R.drawable.sample1, "공원에 있나요?"),
        )
    }

    //예) 새 게시글 추가시 호출할 수 있는 메서드
    fun addPost(post: FeedItem) {
        Log.i(TAG, "새 게시글이 작성 됨(${post.imageResId}, ${post.text})")
        val currentList = mFeedItems.value ?: emptyList()
        mFeedItems.value = currentList + post   //새로운 게시글을 이어서 뒤에 추가 한다.
    }

    companion object {
        private val TAG = FeedViewModel::class.java.simpleName
    }

}