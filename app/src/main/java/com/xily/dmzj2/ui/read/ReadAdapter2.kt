package com.xily.dmzj2.ui.read

import android.view.View
import android.widget.ImageView
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseAdapter
import com.xily.dmzj2.utils.load
import kotlinx.android.synthetic.main.layout_item_read2.view.*

/**
 * Created by Xily on 2019/10/6.
 */
class ReadAdapter2(list: List<String>?) : BaseAdapter<ReadAdapter2.ViewHolder, String>(list) {
    override fun getLayoutId(): Int {
        return R.layout.layout_item_read2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, value: String) {
        holder.image.load(value)
    }

    class ViewHolder(itemView: View) : BaseAdapter.BaseViewHolder(itemView) {
        val image: ImageView = itemView.iv_img
    }
}