package com.xily.dmzj2.ui.read

import android.content.Context
import android.os.BatteryManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseActivity
import com.xily.dmzj2.data.remote.model.ComicBean
import com.xily.dmzj2.utils.hideStatusBar
import com.xily.dmzj2.utils.toast
import kotlinx.android.synthetic.main.activity_read.*
import kotlinx.android.synthetic.main.layout_item_read_border.view.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ReadActivity : BaseActivity() {
    private lateinit var adapter: ReadAdapter2
    private val readViewModel: ReadViewModel by viewModel()
    private val imageList = arrayListOf<String>()
    private var position = 0
    private var comicId = 0
    private var isLoading = false
    private var chapters = arrayListOf<ComicBean.Chapter.Data>()
    private var chapterPosition = arrayListOf<Int>() //保存每一话第一张图片的下标,用于计算当前章数和页数
    override fun getLayoutId(): Int {
        return R.layout.activity_read
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolBar()
        initRecycleView()
        comicId = intent.getIntExtra("comicId", 0)
        val chapterId = intent.getIntExtra("chapterId", 0)
        position = intent.getIntExtra("position", -1)
        chapters.addAll(intent.getParcelableArrayListExtra("chapters"))
        if (position == -1 && chapters.isNotEmpty()) {
            chapters.forEachIndexed { index, data ->
                if (data.chapter_id == chapterId) {
                    position = index
                    return@forEachIndexed
                }
            }
        }
        isLoading = true
        readViewModel.getChapter(comicId.toString(), chapterId.toString()).observe(this, Observer {
            imageList.addAll(it.page_url)
            chapterPosition.add(0)
            adapter.notifyDataSetChanged()
            isLoading = false
        })
        getBatteryAndTime()
    }

    private fun initToolBar() {
        hideStatusBar(window, true)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun getBatteryAndTime() {
        launch(Dispatchers.Default) {
            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            while (true) {
                val battery =
                    batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                withContext(Dispatchers.Main) {
                    tv_battery.text = "$battery%"
                }
                delay(1000 * 60)
            }
        }
        launch(Dispatchers.Default) {
            while (true) {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                withContext(Dispatchers.Main) {
                    tv_time.text = "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
                }
                delay(1000)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecycleView() {
        recycle.layoutManager = LinearLayoutManager(this)
        adapter = ReadAdapter2(imageList)
        val footerView = layoutInflater.inflate(R.layout.layout_item_read_border, recycle, false)
        footerView.tv_tip.text = "正在加载下一话..."
        adapter.footerView = footerView
        adapter.setOnItemClickListener { position ->
            //hideStatusBar(window,false)
            if (toolbar.visibility == View.INVISIBLE) {
                toolbar.visibility = View.VISIBLE
                //给recyclerView设置marginTop,防止toolbar挡住图片
                val layoutParams = recycle.layoutParams as FrameLayout.LayoutParams
                layoutParams.topMargin = toolbar.measuredHeight
                recycle.layoutParams = layoutParams
                hideStatusBar(window, false)
            } else {
                toolbar.visibility = View.INVISIBLE
                val layoutParams = recycle.layoutParams as FrameLayout.LayoutParams
                layoutParams.topMargin = 0
                recycle.layoutParams = layoutParams
                hideStatusBar(window, true)
            }
        }
        recycle.adapter = adapter
        recycle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
                if (layoutManager != null) {
                    //如果出现预留的footerView且不是在加载中就加载下一话
                    if (!isLoading && position > 0 && layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount - 1) {
                        isLoading = true
                        readViewModel.getChapter(
                            comicId.toString(),
                            chapters[position - 1].chapter_id.toString()
                        ).observe(this@ReadActivity, Observer {
                            chapterPosition.add(imageList.size)
                            imageList.addAll(it.page_url)
                            adapter.notifyDataSetChanged()
                            position--//因为越新的章节下标越小,所以position要减
                            isLoading = false
                            toast("以为您自动加载" + chapters[position].chapter_title)
                        })
                    }
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    tv_chapter.text = getChapterTitle(firstVisibleItemPosition)
                    tv_page.text = getPage(firstVisibleItemPosition)
                }
            }
        })
    }

    private fun getChapterTitle(pos: Int): String {
        if (chapterPosition.isEmpty()) return ""
        var tmp = 0
        //遍历下标列表获取当前position位于哪个章节中
        chapterPosition.forEachIndexed { index, i ->
            if (i <= pos) {
                tmp = index
            } else return@forEachIndexed
        }
        //position + chapterPosition.size - 1 还原初始章节position
        //初始章节position-现在章节index就是正在看的章节position
        return chapters[position + chapterPosition.size - 1 - tmp].chapter_title
    }

    private fun getPage(pos: Int): String {
        if (chapterPosition.isEmpty()) return ""
        var tmp = 0
        //遍历下标列表获取当前position位于哪个章节中
        chapterPosition.forEachIndexed { index, i ->
            if (i <= pos) {
                tmp = index
            } else return@forEachIndexed
        }
        val totalPages = if (tmp < chapterPosition.size - 1) {
            chapterPosition[tmp + 1] - chapterPosition[tmp]
        } else {
            imageList.size - chapterPosition[tmp]
        }
        val page = pos - chapterPosition[tmp] + 1
        return "$page/$totalPages"
    }
}