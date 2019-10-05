package com.xily.dmzj2.ui.main

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
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
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeIndexFragment : BaseFragment() {
    private val homeIndexViewModel: HomeIndexViewModel by viewModel()


    override fun getLayoutId(): Int {
        return R.layout.fragment_home_index
    }

    override fun initViews(savedInstanceState: Bundle?) {
        swipe.setColorSchemeColors(getAttrColor(R.attr.colorAccent))
        banner.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context, path: Any?, imageView: ImageView) {
                imageView.load(path)
            }
        })
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
        homeIndexViewModel.getRecommend().observe(this, Observer {
            if (it != null) {
                showData(it)
            } else {
                toastError("获取推荐列表失败!")
            }
        })
    }

    private fun showData(list: List<RecommendBean>) {
        val titles = arrayListOf<String>()
        val imageUrls = arrayListOf<String>()
        val types = arrayListOf<Int>()
        val data0 = list[0].data
        data0.forEach {
            titles.add(it.title)
            imageUrls.add(it.cover)
        }
        banner.setBannerTitles(titles)
        banner.setImages(imageUrls)
        banner.setOnBannerListener {
            val data = data0[it]
            when (data.type) {
                1 -> {
                    val bundle = Bundle()
                    bundle.putInt("id", data.id)
                    startActivity<InfoActivity>(bundle)
                }
                7 -> {
                    val bundle = Bundle()
                    bundle.putString("url", data.url)
                    startActivity<BrowserActivity>(bundle)
                }
            }

        }
        banner.start()
    }

    companion object {
        fun newInstance(): HomeIndexFragment {
            return HomeIndexFragment()
        }
    }
}