package com.xily.dmzj2.ui.read

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseActivity
import com.xily.dmzj2.utils.hideStatusBar
import kotlinx.android.synthetic.main.activity_read.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReadActivity : BaseActivity() {
    private lateinit var adapter: ReadAdapter
    private val readViewModel: ReadViewModel by viewModel()
    override fun getLayoutId(): Int {
        return R.layout.activity_read
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolBar()
        initRecycleView()
        val comicId = intent.getIntExtra("comicId", 0)
        val chapterId = intent.getIntExtra("chapterId", 0)
        readViewModel.getChapter(comicId.toString(), chapterId.toString()).observe(this, Observer {
            adapter.submitList(it.page_url)
        })
    }

    private fun initToolBar() {
        hideStatusBar(window, true)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
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
        adapter = ReadAdapter()
        adapter.setOnItemClickListener { position ->
            //hideStatusBar(window,false)
            if (toolbar.visibility == View.INVISIBLE) {
                toolbar.visibility = View.VISIBLE
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
        recycle.layoutManager = LinearLayoutManager(this)
        recycle.adapter = adapter
    }
}