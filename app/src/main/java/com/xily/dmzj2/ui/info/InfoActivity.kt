package com.xily.dmzj2.ui.info

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.Transition
import android.view.Menu
import android.view.MenuItem
import androidx.core.widget.NestedScrollView
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
import com.xily.dmzj2.data.remote.model.ReInfoBean
import com.xily.dmzj2.data.remote.model.SubscribeStatusBean
import com.xily.dmzj2.ui.read.ReadActivity
import com.xily.dmzj2.utils.*
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class InfoActivity : BaseActivity() {
    private var id = 0
    private lateinit var adapter: ChaptersAdapter
    private var isBitmapLoaded = false
    private val infoViewModel: InfoViewModel by viewModel()
    private lateinit var menu: Menu
    private var isSubscribe = false
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
        var oldAlpha = 0
        layout_scroll.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            val alpha = (scrollY / (height) * 255).toInt()
            toolbar.background.mutate().alpha = if (alpha > 255) 255 else alpha
            if (oldAlpha > 150 && alpha <= 150) setLightToolBar()
            else if (oldAlpha <= 150 && alpha > 150) setNormalToolBar()
            oldAlpha = alpha
        }
        //监听转场动画,等转场动画执行完再加载数据
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_info_menu, menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // back button
                return true
            }
            R.id.menu_subscribe -> {

            }
            R.id.menu_download -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setNormalToolBar() {
        setDarkStatusIcon(true)
        val iconColor = getAttrColor(R.attr.colorSecondary)
        for (i in 0 until menu.size()) {
            menu.getItem(i).icon.setTint(iconColor)
        }
        toolbar.setTitleTextColor(Color.parseColor("#212121"))
        toolbar.navigationIcon?.setTint(iconColor)
    }


    private fun setLightToolBar() {
        setDarkStatusIcon(false)
        val iconColor = Color.parseColor("#ffffff")
        for (i in 0 until menu.size()) {
            menu.getItem(i).icon.setTint(iconColor)
        }
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"))
        toolbar.navigationIcon?.setTint(iconColor)
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
            bundle.putInt("position", position)
            bundle.putParcelableArrayList("chapters", ArrayList(adapter.currentList))
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

    private fun loadData() {
        swipe.isRefreshing = true
        launch(tryBlock = {
            val asyncList = arrayListOf<Deferred<*>>()
            asyncList += async {
                showComic(infoViewModel.getComic(id.toString()))
            }
            asyncList += async {
                showReInfo(infoViewModel.getReadInfo(id.toString()))
            }
            asyncList += async {
                showSubscribeStatus(infoViewModel.getSubscribeStatus(id.toString()))
            }
            asyncList.forEach {
                it.await()
            }
        }, catchBlock = {
            it.printStackTrace()
            toastError("加载详情失败!")
        }, finallyBlock = {
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

    private fun showReInfo(reInfoBean: ReInfoBean) {
        tv_last_read.text = reInfoBean.chapter_name
        layout_last_read.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("comicId", id)
            bundle.putInt("chapterId", reInfoBean.chapter_id)
            bundle.putInt("page", reInfoBean.record)
            bundle.putParcelableArrayList("chapters", ArrayList(adapter.currentList))
            startActivity<ReadActivity>(bundle)
        }
    }

    private fun showSubscribeStatus(subscribeStatusBean: SubscribeStatusBean) {
        if (subscribeStatusBean.code == 0) {
            isSubscribe = true
            menu.findItem(R.id.menu_subscribe).setIcon(R.drawable.ic_favorite_white_24dp)
        } else {
            isSubscribe = false
            menu.findItem(R.id.menu_subscribe).setIcon(R.drawable.ic_favorite_border_white_24dp)
        }
    }
}