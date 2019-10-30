package com.xily.dmzj2.ui.main.home.index

import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseAdapter2
import com.xily.dmzj2.data.remote.model.RecommendBean
import com.xily.dmzj2.utils.gone
import com.xily.dmzj2.utils.load
import com.xily.dmzj2.utils.visible
import kotlinx.android.synthetic.main.layout_item_home_index_1.*

/**
 * Created by Xily on 2019/10/30.
 */
class HomeIndexAdapter1(list: List<RecommendBean.Data>?) :
    BaseAdapter2<RecommendBean.Data>(list) {
    override fun getLayoutId(): Int {
        return R.layout.layout_item_home_index_1
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        value: RecommendBean.Data
    ) {
        holder.iv_cover.load(value.cover)
        holder.tv_title.text = value.title
        if (value.authors.isNotBlank()) {
            holder.tv_more.visible()
            holder.tv_more.text = value.authors
        } else {
            holder.tv_more.gone()
        }
    }
}