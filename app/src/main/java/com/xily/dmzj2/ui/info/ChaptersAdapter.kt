package com.xily.dmzj2.ui.info

import androidx.recyclerview.widget.DiffUtil
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseListAdapter
import com.xily.dmzj2.data.remote.model.ComicBean
import com.xily.dmzj2.databinding.LayoutItemChapterBinding

/**
 * Created by Xily on 2019/10/5.
 */
class ChaptersAdapter :
    BaseListAdapter<ComicBean.Chapter.Data, LayoutItemChapterBinding>(object :
        DiffUtil.ItemCallback<ComicBean.Chapter.Data>() {
        override fun areItemsTheSame(
            oldItem: ComicBean.Chapter.Data,
            newItem: ComicBean.Chapter.Data
        ): Boolean {
            return oldItem.chapter_id == newItem.chapter_id
        }

        override fun areContentsTheSame(
            oldItem: ComicBean.Chapter.Data,
            newItem: ComicBean.Chapter.Data
        ): Boolean {
            return oldItem == newItem
        }
    }) {
    override fun getLayoutId(): Int {
        return R.layout.layout_item_chapter
    }

    override fun bind(binding: LayoutItemChapterBinding, item: ComicBean.Chapter.Data) {
        binding.chapter = item
    }

}