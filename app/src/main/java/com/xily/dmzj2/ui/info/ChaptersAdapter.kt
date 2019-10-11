package com.xily.dmzj2.ui.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xily.dmzj2.R
import com.xily.dmzj2.data.remote.model.ComicBean
import com.xily.dmzj2.model.ChapterBean
import kotlinx.android.synthetic.main.layout_item_chapter.view.*
import kotlinx.android.synthetic.main.layout_item_chapter_title.view.*

/**
 * Created by Xily on 2019/10/11.
 */
class ChaptersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_HEADER = 0
    val TYPE_TITLE = 1
    val TYPE_CONTENT = 2
    val chapterBean = arrayListOf<ChapterBean>()
    var headerView: View? = null
        set(value) {
            field = value
            notifyItemInserted(0)
        }
    var listener: ((chapterId: Int, chapterIndex: Int, chapterPosition: Int) -> Unit)? = null
    fun setChapters(list: List<ComicBean.Chapter>) {
        chapterBean.clear()
        list.forEachIndexed { index, chapter ->
            chapterBean.add(ChapterBean(TYPE_TITLE, chapter.title))
            chapter.data.forEachIndexed { index2, data ->
                chapterBean.add(ChapterBean(TYPE_CONTENT, data.chapter_title).apply {
                    chapterId = data.chapter_id
                    chapterIndex = index
                    chapterPosition = index2
                })
            }
        }
        notifyDataSetChanged()
    }

    private fun onBindViewHolder(holder: TitleViewHolder, position: Int, value: ChapterBean) {
        holder.title.text = value.title
    }

    private fun onBindViewHolder(holder: ContentViewHolder, position: Int, value: ChapterBean) {
        holder.name.text = value.title
        holder.itemView.setOnClickListener {
            listener?.invoke(value.chapterId, value.chapterIndex, value.chapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                HeaderViewHolder(headerView!!)
            }
            TYPE_TITLE -> {
                TitleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_item_chapter_title,
                        parent,
                        false
                    )
                )
            }
            TYPE_CONTENT -> {
                ContentViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_item_chapter,
                        parent,
                        false
                    )
                )
            }
            else -> throw RuntimeException()
        }
    }

    override fun getItemCount(): Int {
        return chapterBean.size + if (headerView != null) 1 else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pos = if (headerView != null) position - 1 else position
        when (getItemViewType(position)) {
            TYPE_TITLE -> onBindViewHolder(holder as TitleViewHolder, pos, chapterBean[pos])
            TYPE_CONTENT -> onBindViewHolder(holder as ContentViewHolder, pos, chapterBean[pos])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (headerView != null && position == 0) TYPE_HEADER else chapterBean[position - 1].type
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.tv_title
    }

    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.tv_name
    }
}