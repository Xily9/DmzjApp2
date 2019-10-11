package com.xily.dmzj2.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.android.material.navigation.NavigationView
import com.jeremyliao.liveeventbus.LiveEventBus
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseActivity
import com.xily.dmzj2.ui.history.HistoryFragment
import com.xily.dmzj2.ui.login.LoginActivity
import com.xily.dmzj2.ui.subscribe.SubscribeFragment
import com.xily.dmzj2.ui.user.UserActivity
import com.xily.dmzj2.utils.startActivity
import com.xily.dmzj2.utils.toastInfo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_side_menu.view.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var exitTime: Long = 0
    private lateinit var fragments: Array<Fragment>
    private var currentTabIndex = 0
    private var index = 0
    private lateinit var mainViewModel: MainViewModel
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews(savedInstanceState: Bundle?) {
        mainViewModel = getViewModel()
        initToolBar()
        initNavigationView()
        initFragment()
        initLiveEventBus()
    }

    private fun initLiveEventBus() {
        LiveEventBus.get("recreate", Boolean::class.java)
            .observe(this, Observer {
                if (it) recreate()
            })
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun initFragment() {
        val homeFragment = HomeFragment.newInstance()
        val historyFragment = HistoryFragment.newInstance()
        val subscribeFragment = SubscribeFragment.newInstance()
        fragments = arrayOf(homeFragment, historyFragment, subscribeFragment)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment, fragments[currentTabIndex])
            .commit()
        setToolbarTitle()
    }

    private fun setToolbarTitle() {
        title = when (currentTabIndex) {
            0 -> "主页"
            1 -> "历史"
            2 -> "订阅"
            else -> ""
        }
    }

    private fun switchFragment() {
        val trx = supportFragmentManager.beginTransaction()
        trx.hide(fragments[currentTabIndex])
        if (!fragments[index].isAdded) {
            trx.add(R.id.fragment, fragments[index])
        }
        trx.show(fragments[index]).commit()
        currentTabIndex = index
        setToolbarTitle()
    }

    private fun changeFragmentIndex(currentIndex: Int) {
        index = currentIndex
        switchFragment()
    }

    private fun initNavigationView() {
        val mDrawerToggle =
            ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.open, R.string.close)
        mDrawerToggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        val sideMenuView = nav_view.inflateHeaderView(R.layout.layout_side_menu)
        mainViewModel.getUserData()?.let {
            val glideUrl = GlideUrl(
                it.photo, LazyHeaders.Builder()
                    .addHeader("Referer", "http://m.dmzj.com")
                    .build()
            )
            Glide.with(this).load(glideUrl).into(sideMenuView.face)
            sideMenuView.nickname.text = it.nickname
            sideMenuView.face.setOnClickListener {
                startActivity<UserActivity>()
            }
        } ?: let {
            sideMenuView.face.setOnClickListener {
                startActivity<LoginActivity>()
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        lifecycleScope.launch {
            delay(250)
            when (item.itemId) {
                R.id.nav_home -> changeFragmentIndex(0)
                R.id.nav_history -> changeFragmentIndex(1)
                R.id.nav_subscribe -> changeFragmentIndex(2)
                //R.id.nav_setting -> startActivity<SettingsActivity>()
                R.id.nav_theme -> {
                }
                //R.id.nav_about -> startActivity<AboutActivity>()
            }
        }
        return true
    }


    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(
                GravityCompat.START
            )
            System.currentTimeMillis() - exitTime < 2000 -> super.onBackPressed()
            else -> {
                toastInfo("再按一次返回键退出程序")
                exitTime = System.currentTimeMillis()
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {

    }
}