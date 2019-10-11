package com.xily.dmzj2.ui.read

import androidx.recyclerview.widget.DiffUtil
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseListAdapter
import com.xily.dmzj2.databinding.LayoutItemReadBinding
import com.xily.dmzj2.utils.load

/**
 * Created by Xily on 2019/10/5.
 */
class ReadAdapter :
    BaseListAdapter<String, LayoutItemReadBinding>(object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }) {
    override fun getLayoutId(): Int {
        return R.layout.layout_item_read
    }

    override fun bind(
        holder: BaseViewHolder<LayoutItemReadBinding>,
        item: String
    ) {
        holder.binding.ivImg.load(item)
    }

}