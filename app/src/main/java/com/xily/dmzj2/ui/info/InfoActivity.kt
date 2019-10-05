package com.xily.dmzj2.ui.info

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.Transition
import android.view.MenuItem
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseActivity
import com.xily.dmzj2.data.remote.model.ComicBean
import com.xily.dmzj2.ui.read.ReadActivity
import com.xily.dmzj2.utils.*
import kotlinx.android.synthetic.main.activity_info.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class InfoActivity : BaseActivity() {
    private var id = 0
    private lateinit var adapter: ChaptersAdapter
    private var isBitmapLoaded = false
    private val infoViewModel: InfoViewModel by viewModel()
    override fun getLayoutId(): Int {
        return R.layout.activity_info
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolBar()
        initRecycleView()
        swipe.setColorSchemeColors(getAttrColor(R.attr.colorAccent))
        swipe.setOnRefreshListener {
            loadData()
        }
        //initViewPager()
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        val actionbarSizeTypedArray = obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
        val height = dp2px(200f) - actionbarSizeTypedArray.getDimension(0, 0f) - statusBarHeight
        actionbarSizeTypedArray.recycle()
        debug(msg = height.toInt())
        id = intent.getIntExtra("id", 0)
        val bytes = intent.getByteArrayExtra("bitmap")
        if (bytes != null && bytes.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            iv_cover.setImageBitmap(bitmap)
            iv_background.setImageBitmap(rsBulr(this, bitmap, 25f, 1f))
            isBitmapLoaded = true
        }
        layout_scroll.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            val alpha = (scrollY / (height) * 255).toInt()
            toolbar.background.mutate().alpha = if (alpha > 255) 255 else alpha
        }
        window.sharedElementEnterTransition.addListener(object :
            Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                loadData()
            }

            override fun onTransitionResume(transition: Transition?) {
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionCancel(transition: Transition?) {
            }

            override fun onTransitionStart(transition: Transition?) {
            }

        })
    }

    /* private fun initViewPager() {
         viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
             override fun getItem(position: Int): Fragment {
                 return HomeIndexFragment.newInstance()
             }

             override fun getCount(): Int {
                 return 2
             }

             override fun getPageTitle(position: Int): CharSequence? {
                 return when (position) {
                     0 -> "章节"
                     1 -> "评论"
                     else -> ""
                 }
             }
         }
         layout_tab.setupWithViewPager(viewPager)
     }*/

    private fun initRecycleView() {
        adapter = ChaptersAdapter()
        adapter.setOnItemClickListener { position ->
            val bundle = Bundle()
            bundle.putInt("comicId", id)
            bundle.putInt("chapterId", adapter.currentList[position].chapter_id)
            startActivity<ReadActivity>(bundle)
        }
        recycle_chapter.layoutManager = GridLayoutManager(this, 4)
        recycle_chapter.adapter = adapter
        recycle_chapter.isNestedScrollingEnabled = false
    }

    private fun initToolBar() {
        setStatusBarUpper()
        setSupportActionBar(toolbar)
        toolbar.background.mutate().alpha = 0
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // back button
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData() {
        swipe.isRefreshing = true
        infoViewModel.getComic(id.toString()).observe(this, Observer {
            if (it != null) {
                showComic(it)
            } else {
                toastError("加载详情失败!")
            }
            swipe.isRefreshing = false
        })
    }

    private fun showComic(comicBean: ComicBean) {
        tv_content.text = comicBean.description
        tv_author.text = ""
        comicBean.authors.forEach {
            tv_author.append(it.tag_name + " ")
        }
        tv_type.text = ""
        comicBean.types.forEach {
            tv_type.append(it.tag_name + " ")
        }
        tv_subscribe.text = "订阅 ${comicBean.subscribe_num}"
        tv_hot.text = "人气 ${comicBean.hot_num}"
        tv_update.text =
            SimpleDateFormat("yyyy-MM-dd ", Locale.CHINA).format(comicBean.last_updatetime * 1000L)
        comicBean.status.forEach {
            tv_update.append(it.tag_name + " ")
        }
        if (!isBitmapLoaded) {
            val img = R.drawable.ic_default_image_vertical
            val options = RequestOptions()
                .placeholder(img)
                .error(img)
            val glideUrl = GlideUrl(
                comicBean.cover, LazyHeaders.Builder()
                    .addHeader("Referer", "http://m.dmzj.com")
                    .build()
            )
            Glide.with(this).load(glideUrl).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    val bitmap = (resource as BitmapDrawable).bitmap
                    iv_background.setImageBitmap(rsBulr(this@InfoActivity, bitmap, 25f, 1f))
                    return false
                }
            }).apply(options).into(iv_cover)
        }
        title = comicBean.title
        adapter.submitList(comicBean.chapters[0].data)
    }
}