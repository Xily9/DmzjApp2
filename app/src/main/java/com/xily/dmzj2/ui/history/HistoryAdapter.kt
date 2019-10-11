package com.xily.dmzj2.ui.history

import androidx.recyclerview.widget.DiffUtil
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseListAdapter
import com.xily.dmzj2.data.remote.model.HistoryBean
import com.xily.dmzj2.databinding.LayoutItemHistoryBinding
import com.xily.dmzj2.utils.load
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Xily on 2019/10/7.
 */
class HistoryAdapter : BaseListAdapter<HistoryBean, LayoutItemHistoryBinding>(object :
    DiffUtil.ItemCallback<HistoryBean>() {
    override fun areItemsTheSame(oldItem: HistoryBean, newItem: HistoryBean): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: HistoryBean, newItem: HistoryBean): Boolean {
        return oldItem == newItem
    }
}) {

    var buttonClickListener: ((position: Int) -> Unit)? = null
    override fun getLayoutId(): Int {
        return R.layout.layout_item_history
    }

    override fun bind(holder: BaseViewHolder<LayoutItemHistoryBinding>, item: HistoryBean) {
        holder.binding.image.load(item.cover)
        holder.binding.tvTitle.text = item.comic_name
        holder.binding.tvHistory.text = "看到：${item.chapter_name} 第${item.record}页"
        holder.binding.tvTime.text = "时间：${SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.CHINA
        ).format(item.viewing_time * 1000L)}"
        holder.binding.button.setOnClickListener {
            buttonClickListener?.invoke(holder.adapterPosition)
        }
    }
}