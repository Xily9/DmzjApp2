package com.xily.dmzj2.ui.info

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.AppBarLayout
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseActivity
import com.xily.dmzj2.data.remote.model.ComicBean
import com.xily.dmzj2.data.remote.model.ReInfoBean
import com.xily.dmzj2.data.remote.model.SubscribeStatusBean
import com.xily.dmzj2.ui.read.ReadActivity
import com.xily.dmzj2.utils.*
import kotlinx.android.synthetic.main.activity_info.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*


class InfoActivity : BaseActivity() {
    private var id = 0
    private var isBitmapLoaded = false
    private lateinit var infoViewModel: InfoViewModel
    private lateinit var menu: Menu
    private var isSubscribe = false
    private var chapters = arrayListOf<ComicBean.Chapter.Data>()
    override fun getLayoutId(): Int {
        return R.layout.activity_info
    }

    override fun initViews(savedInstanceState: Bundle?) {
        /*var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        val actionbarSizeTypedArray = obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
        val height = dp2px(200f) - actionbarSizeTypedArray.getDimension(0, 0f) - statusBarHeight
        actionbarSizeTypedArray.recycle()
        debug(msg = height.toInt())*/
        id = intent.getIntExtra("id", 0)
        infoViewModel = getViewModel {
            parametersOf(id)
        }
        initToolBar()
        initViewPager()
        val bytes = intent.getByteArrayExtra("bitmap")
        if (bytes != null && bytes.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            iv_cover.setImageBitmap(bitmap)
            iv_background.setImageBitmap(rsBulr(this, bitmap, 25f, 1f))
            isBitmapLoaded = true
        }
        /* var oldAlpha = 0
         layout_scroll.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
             val alpha = (scrollY / (height) * 255).toInt()
             toolbar.background.mutate().alpha = if (alpha > 255) 255 else alpha
             if (oldAlpha > 150 && alpha <= 150) setLightToolBar()
             else if (oldAlpha <= 150 && alpha > 150) setNormalToolBar()
             oldAlpha = alpha
         }*/
        loadData()
        /*//监听转场动画,等转场动画执行完再加载数据
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

        })*/
        var oldOffset = 0
        var critical = 300
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (-oldOffset > critical && -verticalOffset <= critical) setLightToolBar()
            else if (-oldOffset <= critical && -verticalOffset > critical) setNormalToolBar()
            oldOffset = verticalOffset
        })
        ViewCompat.setOnApplyWindowInsetsListener(appBarLayout) { _, insets ->
            // Instead of
            // toolbar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
            (toolbar.layoutParams as ViewGroup.MarginLayoutParams).topMargin =
                insets.systemWindowInsetTop
            insets.consumeSystemWindowInsets()
        }
    }

    private fun initData() {
        infoViewModel.comicBean.observe(this, Observer {
            if (it != null) {
                showComic(it)
            } else {
                toastError("获取漫画信息失败!")
            }
        })
        infoViewModel.subscribeStatusBean.observe(this, Observer {
            it?.let {
                showSubscribeStatus(it)
            }
        })
        infoViewModel.reInfoBean.observe(this, Observer {
            it?.let {
                showReInfo(it)
            }
        })
        infoViewModel.chaptersBean.observe(this, Observer {
            it?.let {
                chapters.addAll(it[0].data)
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        initData()
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_info_menu, menu)
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
            menu.getItem(i).icon.mutate().setTint(iconColor)
        }
        toolbar.setTitleTextColor(Color.parseColor("#212121"))
        toolbar.navigationIcon?.mutate()?.setTint(iconColor)
    }


    private fun setLightToolBar() {
        setDarkStatusIcon(false)
        val iconColor = Color.parseColor("#ffffff")
        for (i in 0 until menu.size()) {
            menu.getItem(i).icon.mutate().setTint(iconColor)
        }
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"))
        toolbar.navigationIcon?.mutate()?.setTint(iconColor)
    }

    private fun initViewPager() {
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            val fragments = arrayOf(ChapterFragment.newInstance(), CommentFragment.newInstance())
            val titles = arrayOf("章节", "评论")
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return 2
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return titles[position]
            }
        }
        tabLayout.setupWithViewPager(viewPager)
        //tabLayout2.setupWithViewPager(viewPager)
    }


    private fun initToolBar() {
        setStatusBarUpper()
        setSupportActionBar(toolbar)
        //toolbar.background.mutate().alpha = 0
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun loadData() {
        infoViewModel.getComic()
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
    }

    private fun showReInfo(reInfoBean: ReInfoBean) {
        tv_last_read.text = reInfoBean.chapter_name
        layout_last_read.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("comicId", id)
            bundle.putInt("chapterId", reInfoBean.chapter_id)
            bundle.putInt("page", reInfoBean.record)
            bundle.putParcelableArrayList("chapters", chapters)
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