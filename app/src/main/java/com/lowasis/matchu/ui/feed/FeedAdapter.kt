package com.lowasis.matchu.ui.feed

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lowasis.matchu.R
import com.lowasis.matchu.databinding.FeedAdapterBinding

class FeedAdapter(private var items: List<FeedItem>) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedViewHolder {
        Log.i(TAG, "onCreateViewHolder!!")
        val binding = FeedAdapterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedAdapter.FeedViewHolder, position: Int) {

        Log.i(TAG, "onBindeViewHolder!!")
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        Log.i(TAG, "getItemCount!!")
        return items.size   //데이터 개수 리턴
    }

    // 외부에서 아이템 업데이트할 때 호출
    fun updateItems(newItems: List<FeedItem>) {
        items = newItems
        notifyDataSetChanged()  //adapter의 바인딩된 전체 데이터가 바뀌었을 때 호출.
    }

    inner class FeedViewHolder(private val binding: FeedAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FeedItem) {
            Log.i(TAG, "bind() item: ${item.text}")

            // ❗ 데이터 → UI 연결
            binding.feedTitle.text = item.text

            // 이미지도 있다면 예시 (Glide 사용 시)
            Glide.with(binding.root)
                .load(item.imageResId)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.imgMain)

            // 항목 클릭 시 BottomSheet 팝업 열기
            binding.root.setOnClickListener {
                val fragment = FeedBottomSheetDetailDialogFragment.newInstance(
                    item.text, item.imageResId
                )

                fragment.show(
                    (binding.root.context as AppCompatActivity).supportFragmentManager,
                    "FeedDetailBottomSheet"
                )
            }
        }
    }

    companion object {
        private val TAG = FeedAdapter::class.java.simpleName
    }
}