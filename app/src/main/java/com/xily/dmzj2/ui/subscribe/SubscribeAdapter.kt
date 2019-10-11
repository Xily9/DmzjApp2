package com.xily.dmzj2.ui.subscribe

import androidx.recyclerview.widget.DiffUtil
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseListAdapter
import com.xily.dmzj2.data.remote.model.SubscribeBean
import com.xily.dmzj2.databinding.LayoutItemSubscribeBinding
import com.xily.dmzj2.utils.load

/**
 * Created by Xily on 2019/10/5.
 */
class SubscribeAdapter : BaseListAdapter<SubscribeBean, LayoutItemSubscribeBinding>(object :
    DiffUtil.ItemCallback<SubscribeBean>() {
    override fun areItemsTheSame(oldItem: SubscribeBean, newItem: SubscribeBean): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SubscribeBean, newItem: SubscribeBean): Boolean {
        return oldItem == newItem
    }
}) {
    override fun getLayoutId(): Int {
        return R.layout.layout_item_subscribe
    }

    override fun bind(holder: BaseViewHolder<LayoutItemSubscribeBinding>, item: SubscribeBean) {
        holder.binding.image.load(item.sub_img)
        holder.binding.text.text = item.name
    }

}