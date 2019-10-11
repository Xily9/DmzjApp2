package com.xily.dmzj2.ui.history

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseFragment
import com.xily.dmzj2.ui.info.InfoActivity
import com.xily.dmzj2.ui.read.ReadActivity
import com.xily.dmzj2.utils.getAttrColor
import com.xily.dmzj2.utils.startActivity
import com.xily.dmzj2.utils.toastError
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.ByteArrayOutputStream

class HistoryFragment : BaseFragment() {
    private lateinit var adapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel
    override fun initViews(savedInstanceState: Bundle?) {
        historyViewModel = getViewModel()
        initSwipeRefreshLayout()
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {
        recycle.layoutManager = LinearLayoutManager(requireActivity())
        adapter = HistoryAdapter()
        adapter.setOnItemClickListener { position, view ->
            val intent = Intent(activity, InfoActivity::class.java)
            val bitmap = (view.image.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, baos)
            val bytes = baos.toByteArray()
            intent.putExtra("bitmap", bytes)
            intent.putExtra("id", adapter.currentList.get(position).comic_id)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity!!,
                    view.image,
                    "image"
                ).toBundle()
            )
        }
        adapter.buttonClickListener = {
            val historyBean = adapter.currentList.get(it)
            val bundle = Bundle()
            bundle.putInt("comicId", historyBean.comic_id)
            bundle.putInt("chapterId", historyBean.chapter_id)
            bundle.putInt("page", historyBean.record)
            startActivity<ReadActivity>(bundle)
        }
        recycle.adapter = adapter
    }

    private fun initSwipeRefreshLayout() {
        activity?.let {
            swipe.setColorSchemeColors(it.getAttrColor(R.attr.colorAccent))
        }
        swipe.setOnRefreshListener {
            loadData()
        }
    }

    private fun loadData() {
        swipe.isRefreshing = true
        historyViewModel.getHistory().observe(this, Observer {
            it.fold({
                adapter.submitList(it)
            }, {
                toastError(it.message ?: "")
            })
            swipe.isRefreshing = false
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_history
    }

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }
}