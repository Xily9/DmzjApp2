package com.xily.dmzj2.ui.info

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseFragment
import com.xily.dmzj2.data.remote.model.ComicBean
import com.xily.dmzj2.ui.read.ReadActivity
import com.xily.dmzj2.utils.getAttrColor
import com.xily.dmzj2.utils.startActivity
import kotlinx.android.synthetic.main.layout_info_pager_chapters.*
import kotlinx.android.synthetic.main.layout_item_chapter_header.*
import kotlinx.android.synthetic.main.layout_item_chapter_header.view.*
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel

/**
 * Created by Xily on 2019/10/10.
 */
class ChapterFragment : BaseFragment() {
    private lateinit var adapter: ChaptersAdapter
    private var chapters = arrayListOf<ComicBean.Chapter>()
    private lateinit var infoViewModel: InfoViewModel
    private var isAscending = false
    override fun getLayoutId(): Int {
        return R.layout.layout_info_pager_chapters
    }

    override fun initViews(savedInstanceState: Bundle?) {
        infoViewModel = getSharedViewModel()
        initRecycleView()
        initData()
    }

    private fun initRecycleView() {
        recycle_chapter.layoutManager = GridLayoutManager(requireActivity(), 4).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.getItemViewType(position) == adapter.TYPE_CONTENT)
                        1
                    else
                        spanCount
                }
            }
        }
        adapter = ChaptersAdapter()
        val headerView =
            layoutInflater.inflate(R.layout.layout_item_chapter_header, recycle_chapter, false)
        headerView.tv_asc.setOnClickListener {
            if (!isAscending) {
                isAscending = true
                tv_asc.setTextColor(getAttrColor(R.attr.colorAccent))
                tv_desc.setTextColor(Color.parseColor("#757575"))
                val list = arrayListOf<ComicBean.Chapter>()
                chapters.forEach {
                    list.add(ComicBean.Chapter(it.title, it.data.asReversed()))
                }
                adapter.setChapters(list)
            }
        }
        headerView.tv_desc.setOnClickListener {
            if (isAscending) {
                isAscending = false
                tv_desc.setTextColor(getAttrColor(R.attr.colorAccent))
                tv_asc.setTextColor(Color.parseColor("#757575"))
                adapter.setChapters(chapters)
            }
        }
        adapter.headerView = headerView
        adapter.listener = { chapterId, chapterIndex, chapterPosition ->
            startActivity<ReadActivity>(
                "comicId" to infoViewModel.comicId,
                "chapterId" to chapterId,
                "position" to if (isAscending) chapters[chapterIndex].data.size - 1 - chapterPosition else chapterPosition,
                "chapters" to ArrayList(chapters[chapterIndex].data)
            )
        }
        recycle_chapter.adapter = adapter
        //recycle_chapter.isNestedScrollingEnabled = false
    }

    private fun initData() {
        infoViewModel.chaptersBean.observe(this, Observer {
            chapters.clear()
            chapters.addAll(it)
            adapter.setChapters(chapters)
        })
    }

    companion object {
        fun newInstance() = ChapterFragment()
    }
}