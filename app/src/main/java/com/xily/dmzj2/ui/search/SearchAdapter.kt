package com.xily.dmzj2.ui.search

import androidx.recyclerview.widget.DiffUtil
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseListAdapter
import com.xily.dmzj2.data.remote.model.SearchBean
import com.xily.dmzj2.databinding.LayoutItemSearchBinding
import com.xily.dmzj2.utils.load

/**
 * Created by Xily on 2019/10/5.
 */
class SearchAdapter : BaseListAdapter<SearchBean, LayoutItemSearchBinding>(object :
    DiffUtil.ItemCallback<SearchBean>() {
    override fun areItemsTheSame(oldItem: SearchBean, newItem: SearchBean): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SearchBean, newItem: SearchBean): Boolean {
        return oldItem == newItem
    }
}) {
    override fun getLayoutId(): Int {
        return R.layout.layout_item_search
    }

    override fun bind(binding: LayoutItemSearchBinding, item: SearchBean) {
        binding.data = item
        binding.ivCover.load(item.cover)
    }

}