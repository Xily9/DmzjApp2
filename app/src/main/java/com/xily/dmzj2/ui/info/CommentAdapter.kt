package com.xily.dmzj2.ui.info

import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseAdapter2
import com.xily.dmzj2.data.remote.model.CommentBean
import com.xily.dmzj2.utils.load
import com.xily.dmzj2.utils.toTimeStr
import kotlinx.android.synthetic.main.layout_item_comment.*

/**
 * Created by Xily on 2019/10/13.
 */
class CommentAdapter(list: List<CommentBean.Comment>?) : BaseAdapter2<CommentBean.Comment>(list) {
    override fun getLayoutId(): Int {
        return R.layout.layout_item_comment
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        value: CommentBean.Comment
    ) {
        holder.tv_nickname.text = value.nickname
        holder.iv_face.load(value.avatar_url)
        holder.tv_content.text = value.content
        holder.tv_zan.text = value.like_amount
        holder.tv_time.text = value.create_time.toInt().toTimeStr()
    }
}