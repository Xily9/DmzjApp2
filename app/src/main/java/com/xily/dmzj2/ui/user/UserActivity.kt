package com.xily.dmzj2.ui.user

import android.os.Bundle
import android.view.MenuItem
import com.jeremyliao.liveeventbus.LiveEventBus
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseActivity
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserActivity : BaseActivity() {
    private val userViewModel: UserViewModel by viewModel()
    override fun getLayoutId(): Int {
        return R.layout.activity_user
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolBar()
        btn_logout.setOnClickListener {
            userViewModel.logout()
            LiveEventBus.get("recreate").post(true)
            finish()
        }
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        title = "用户中心"
        val actionBar = supportActionBar
        actionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish() // back button
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}