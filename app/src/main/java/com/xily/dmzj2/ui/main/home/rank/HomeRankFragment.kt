package com.xily.dmzj2.ui.main.home.rank

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
import com.xily.dmzj2.utils.getAttrColor
import kotlinx.android.synthetic.main.fragment_home_rank.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.ByteArrayOutputStream

class HomeRankFragment : BaseFragment() {
    private lateinit var adapter: HomeRankAdapter
    private lateinit var homeRankViewModel: HomeRankViewModel
    override fun initViews(savedInstanceState: Bundle?) {
        homeRankViewModel = getViewModel()
        initRecyclerView()
        initSwipeRefreshLayout()
        loadData()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home_rank
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        adapter = HomeRankAdapter()
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
        recyclerView.adapter = adapter
    }

    private fun initSwipeRefreshLayout() {
        swipe.setColorSchemeColors(getAttrColor(R.attr.colorAccent))
        swipe.setOnRefreshListener {
            loadData()
        }
    }

    private fun loadData() {
        swipe.isRefreshing = true
        homeRankViewModel.getRank("0", "0", "0", "0").observe(this, Observer {
            adapter.submitList(it)
            swipe.isRefreshing = false
        })
    }

    companion object {
        fun newInstance() = HomeRankFragment()
    }
}