package com.lowasis.matchu.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lowasis.matchu.databinding.FragmentFeedBinding

/* Feed 화면 UI */
class FeedFragment: Fragment() {
    private lateinit var adapter: FeedAdapter
    private lateinit var binding: FragmentFeedBinding

    //ViewModel 주입 (Fragment범위)
    private val viewModel: FeedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(layoutInflater)

        Log.i(TAG, "onCreateView()!")

        //RecyclerView 설정
        adapter = FeedAdapter(emptyList())

        // 수직 방향 리스트 레이아웃 설정
        binding.feedRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.feedRecyclerView.adapter = adapter

        //ViewModel의 LiveData를 observe
        viewModel.feedItems.observe(viewLifecycleOwner) { itemList ->
            adapter.updateItems(itemList)
        }

        return binding.root
    }

    companion object {
        private val TAG = FeedFragment::class.java.simpleName
    }

}