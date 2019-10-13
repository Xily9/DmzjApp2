package com.xily.dmzj2.ui.info

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseFragment
import com.xily.dmzj2.data.remote.model.CommentBean
import com.xily.dmzj2.utils.getAttrColor
import kotlinx.android.synthetic.main.layout_info_pager_comment.*
import kotlinx.android.synthetic.main.layout_item_footer.view.*
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel

/**
 * Created by Xily on 2019/10/10.
 */
class CommentFragment : BaseFragment() {
    private lateinit var infoViewModel: InfoViewModel
    private lateinit var adapter: CommentAdapter
    private var list = arrayListOf<CommentBean.Comment>()
    private var time: Long = 0
    private var isLoading = true
    private var page = 1
    override fun getLayoutId(): Int {
        return R.layout.layout_info_pager_comment
    }

    override fun initViews(savedInstanceState: Bundle?) {
        infoViewModel = getSharedViewModel()
        initRecyclerView()
        swipe.setColorSchemeColors(getAttrColor(R.attr.colorAccent))
        swipe.setOnRefreshListener {
            page = 1
            loadData(true)
        }
        loadData(true)
    }

    private fun initRecyclerView() {
        adapter = CommentAdapter(list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val headerView =
            layoutInflater.inflate(R.layout.layout_item_comment_header, recyclerView, false)
        adapter.headerView = headerView
        val footView = layoutInflater.inflate(R.layout.layout_item_footer, recyclerView, false)
        adapter.footerView = footView
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (!isLoading && System.currentTimeMillis() - time > 500 && dy > 0 && layoutManager.itemCount < layoutManager.findLastVisibleItemPosition() + 3) {
                    time = System.currentTimeMillis()
                    page++
                    loadData(false)
                }
            }
        })
    }

    private fun loadData(isRefreshing: Boolean = false) {
        launch(tryBlock = {
            if (isRefreshing) {
                swipe.isRefreshing = true
            } else {
                showLoading()
            }
            val comments = infoViewModel.getComment(page.toString(), "20")
            comments.commentIds.forEach {
                comments.comments[it]?.let {
                    list.add(it)
                }
            }
            adapter.notifyDataSetChanged()
            showComplete()
        }, catchBlock = {
            showError()
        }, finallyBlock = {
            if (isRefreshing) swipe.isRefreshing = false
        })
    }

    private fun showLoading() {
        isLoading = true
        adapter.footerView?.apply {
            layout_default.visibility = View.GONE
            layout_loading.visibility = View.VISIBLE
            layout_error.visibility = View.GONE
        }
    }

    private fun showError() {
        isLoading = false
        adapter.footerView?.apply {
            layout_error.visibility = View.VISIBLE
            layout_default.visibility = View.GONE
            layout_loading.visibility = View.GONE
        }
    }

    private fun showComplete() {
        isLoading = false
        adapter.footerView?.apply {
            layout_error.visibility = View.GONE
            layout_loading.visibility = View.GONE
            layout_default.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance() = CommentFragment()
    }
}