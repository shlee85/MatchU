package com.lowasis.matchu.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lowasis.matchu.databinding.FragmentFeedDetailBottomSheetBinding

class FeedBottomSheetDetailDialogFragment: BottomSheetDialogFragment() {
    private var _binding: FragmentFeedDetailBottomSheetBinding ?= null
    private val binding get() = _binding!!

    companion object {
        private val TAG = FeedBottomSheetDetailDialogFragment::class.java.simpleName
        private const val FEED_TITLE = "title"
        private const val FEED_IMG_URL = "imageUrl"

        /**
         * Feed 아이템 데이터를 팝업에 넘기기 위한 팩토리 메서드
         * @param title 제목 텍스트
         * @param imageResId 이미지 리소스 ID (Int)
         */
        fun newInstance(title: String, imageUrl: Int): FeedBottomSheetDetailDialogFragment {
            Log.i(TAG, "newInstance")
            val fragment = FeedBottomSheetDetailDialogFragment()
            val args = Bundle().apply {
                putString(FEED_TITLE, title)
                putInt(FEED_IMG_URL, imageUrl)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedDetailBottomSheetBinding.inflate(inflater, container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super는 부모 클래스의 기본 초기화 로직을 실행 하기 위한 용도 (대부분 라이플사이클 함수에서 사용)
        super.onViewCreated(view, savedInstanceState)

        // 전달받은 데이터 가져오기
        val title = arguments?.getString(FEED_TITLE) ?: ""
        val imageResId = arguments?.getInt(FEED_IMG_URL) ?: 0

        binding.titleDetail.text = title
        Glide.with(this)
            .load(imageResId)
            .into(binding.imgDetail)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}