package com.xily.dmzj2.ui.main.home.index

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseFragment
import com.xily.dmzj2.data.remote.model.RecommendBean
import com.xily.dmzj2.ui.common.BrowserActivity
import com.xily.dmzj2.ui.info.InfoActivity
import com.xily.dmzj2.utils.getAttrColor
import com.xily.dmzj2.utils.load
import com.xily.dmzj2.utils.startActivity
import com.xily.dmzj2.utils.toastError
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_home_index.*
import kotlinx.android.synthetic.main.layout_index_item_header.view.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class HomeIndexFragment : BaseFragment() {
    private lateinit var homeIndexViewModel: HomeIndexViewModel
    override fun getLayoutId(): Int {
        return R.layout.fragment_home_index
    }

    override fun initViews(savedInstanceState: Bundle?) {
        homeIndexViewModel = getViewModel()
        swipe.setColorSchemeColors(getAttrColor(R.attr.colorAccent))
        swipe.setOnRefreshListener {
            loadData()
        }
        banner.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context, path: Any?, imageView: ImageView) {
                imageView.load(path)
            }
        })
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
        homeIndexViewModel.recommend.observe(this, Observer {
            if (it != null) {
                linearLayout.removeAllViews()
                showData(it)
            } else {
                toastError("获取推荐列表失败!")
            }
            swipe.isRefreshing = false
        })
        loadData()
    }

    private fun loadData() {
        swipe.isRefreshing = true
        homeIndexViewModel.getRecommend()
    }

    private fun showData(list: List<RecommendBean>) {
        list.sortedBy { it.sort }.forEach {
            when (it.category_id) {
                46 -> showBanner(it)//大图推荐
                47 -> {//近期必看
                    insertHeader(it.title, R.drawable.iv_index_recent)
                    insertRecyclerView(1, it.data)
                }
                48 -> {//火热专题
                    insertHeader(it.title, R.drawable.iv_index_hot, R.drawable.iv_index_more_s, {

                    })
                    insertRecyclerView(2, it.data)
                }
                49 -> {//我的订阅
                    insertHeader(it.title, R.drawable.iv_index_order_refresh)
                    insertRecyclerView(1, it.data)
                }
                50 -> {//猜你喜欢
                    insertHeader(
                        it.title,
                        R.drawable.iv_index_youlike,
                        R.drawable.iv_index_refresh_s,
                        {

                        })
                    insertRecyclerView(1, it.data)
                }
                51 -> {//大师级
                    insertHeader(it.title, R.drawable.iv_index_master_work)
                    insertRecyclerView(1, it.data)
                }
                52 -> {//国漫
                    insertHeader(
                        it.title,
                        R.drawable.iv_index_inner_cartoon,
                        R.drawable.iv_index_refresh_s,
                        {

                        })
                    insertRecyclerView(1, it.data)
                }
                53 -> {//美漫
                    insertHeader(it.title, R.drawable.iv_index_americ_eve)
                    insertRecyclerView(2, it.data)
                }
                54 -> {//热门连载
                    insertHeader(
                        it.title,
                        R.drawable.iv_index_hot_serial,
                        R.drawable.iv_index_refresh_s,
                        {

                        })
                    insertRecyclerView(1, it.data)
                }
                55 -> {//条漫专区
                    insertHeader(it.title, R.drawable.iv_index_strip_cart)
                    insertRecyclerView(2, it.data)
                }
                92 -> {//动画专区
                    insertHeader(
                        it.title,
                        R.drawable.iv_index_pindonghua,
                        R.drawable.iv_index_more_s,
                        {

                        })
                    insertRecyclerView(1, it.data)
                }
                56 -> {//最新上架
                    insertHeader(it.title, R.drawable.iv_index_latest_pub)
                    insertRecyclerView(1, it.data)
                }
            }
        }
    }

    private fun insertHeader(
        title: String, @DrawableRes icon: Int,
        @DrawableRes extraIcon: Int? = null,
        listener: (() -> Unit)? = null
    ) {
        val layout = layoutInflater.inflate(R.layout.layout_index_item_header, linearLayout, false)
        layout.tv_title.text = title
        layout.iv_img.setImageResource(icon)
        extraIcon?.let { layout.iv_more.setImageResource(it) }
        listener?.let { layout.setOnClickListener { listener() } }
        linearLayout.addView(layout)
    }

    private fun insertRecyclerView(type: Int, data: List<RecommendBean.Data>) {
        val recyclerView = RecyclerView(requireContext())
        recyclerView.layoutManager = GridLayoutManager(requireContext(), if (type == 1) 3 else 2)
        recyclerView.isNestedScrollingEnabled = false
        val adapter = if (type == 1) {
            HomeIndexAdapter1(data)
        } else {
            HomeIndexAdapter2(data)
        }
        adapter.setOnItemClickListener {
            handleItemListener(data[it])
        }
        recyclerView.adapter = adapter
        val layoutParams = linearLayout.layoutParams
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        linearLayout.addView(recyclerView, layoutParams)
    }

    private fun showBanner(recommendBean: RecommendBean) {
        val titles = arrayListOf<String>()
        val imageUrls = arrayListOf<String>()
        recommendBean.data.forEach {
            titles.add(it.title)
            imageUrls.add(it.cover)
        }
        banner.setBannerTitles(titles)
        banner.setImages(imageUrls)
        banner.setOnBannerListener {
            handleItemListener(recommendBean.data[it])
        }
        banner.start()
    }

    private fun handleItemListener(item: RecommendBean.Data) {
        when (item.type) {
            0, 1 -> {
                val bundle = Bundle()
                bundle.putInt("id", item.id)
                startActivity<InfoActivity>(bundle)
            }
            7 -> {
                val bundle = Bundle()
                bundle.putString("url", item.url)
                startActivity<BrowserActivity>(bundle)
            }
        }
    }

    companion object {
        fun newInstance(): HomeIndexFragment {
            return HomeIndexFragment()
        }
    }
}