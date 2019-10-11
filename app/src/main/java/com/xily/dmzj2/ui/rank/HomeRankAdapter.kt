package com.xily.dmzj2.ui.rank

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.recyclerview.widget.DiffUtil
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseListAdapter
import com.xily.dmzj2.data.remote.model.RankBean
import com.xily.dmzj2.databinding.LayoutItemRankBinding
import com.xily.dmzj2.utils.load
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Xily on 2019/10/7.
 */
class HomeRankAdapter :
    BaseListAdapter<RankBean, LayoutItemRankBinding>(object : DiffUtil.ItemCallback<RankBean>() {
        override fun areItemsTheSame(oldItem: RankBean, newItem: RankBean): Boolean {
            return oldItem.comic_id == newItem.comic_id
        }

        override fun areContentsTheSame(oldItem: RankBean, newItem: RankBean): Boolean {
            return oldItem == newItem
        }
    }) {
    override fun getLayoutId(): Int {
        return R.layout.layout_item_rank
    }

    override fun bind(holder: BaseViewHolder<LayoutItemRankBinding>, item: RankBean) {
        holder.binding.apply {
            tvTitle.text = item.title
            tvAuthor.text = item.authors
            tvType.text = item.types
            tvTime.text = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(item.last_updatetime)
            image.load(item.cover)
            tvRankNumber.text = (holder.adapterPosition + 1).toString()
            layoutRankNumber.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(
                    when (holder.adapterPosition) {
                        0 -> "#FF4B4C"
                        1 -> "#FF8005"
                        2 -> "#FFC000"
                        else -> "#DFDFDF"
                    }
                )
            )
        }
    }
}