package com.xily.dmzj2.ui.subscribe

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseFragment
import com.xily.dmzj2.ui.info.InfoActivity
import com.xily.dmzj2.utils.getAttrColor
import com.xily.dmzj2.utils.toastError
import kotlinx.android.synthetic.main.fragment_subscribe.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.ByteArrayOutputStream


class SubscribeFragment : BaseFragment() {

    private lateinit var adapter: SubscribeAdapter
    private lateinit var subscribeViewModel: SubscribeViewModel
    override fun getLayoutId(): Int {
        return R.layout.fragment_subscribe
    }

    override fun initViews(state: Bundle?) {
        subscribeViewModel = getViewModel()
        initSwipeRefreshLayout()
        initRecycleView()
        loadData()
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
        subscribeViewModel.getSubscribe().observe(this, Observer {
            it.fold({
                adapter.submitList(it)
            }, {
                toastError(it.message ?: "")
            })
            swipe.isRefreshing = false
        })
    }

    private fun initRecycleView() {
        recycle.layoutManager = GridLayoutManager(activity, 3)
        adapter = SubscribeAdapter()
        adapter.setOnItemClickListener { position, view ->
            val intent = Intent(activity, InfoActivity::class.java)
            val bitmap = (view.image.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, baos)
            val bytes = baos.toByteArray()
            intent.putExtra("bitmap", bytes)
            intent.putExtra("id", adapter.currentList.get(position).id)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity!!,
                    view.image,
                    "image"
                ).toBundle()
            )
        }
        recycle.adapter = adapter
    }

    companion object {
        fun newInstance(): SubscribeFragment {
            return SubscribeFragment()
        }
    }
}