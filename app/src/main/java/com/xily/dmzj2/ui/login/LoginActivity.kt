package com.xily.dmzj2.ui.login

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseActivity
import com.xily.dmzj2.utils.toastError
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class LoginActivity : BaseActivity() {
    private lateinit var loginViewModel: LoginViewModel
    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initViews(savedInstanceState: Bundle?) {
        loginViewModel = getViewModel()
        initToolBar()
        btn_login.setOnClickListener {
            btn_login.startAnimate()
            loginViewModel.login(et_user.text.toString(), et_pass.text.toString()).observe(this,
                Observer {
                    btn_login.stopAnimation()
                    it.fold({
                        LiveEventBus.get("recreate").post(true)
                        finish()
                    }, {
                        toastError(it.message ?: "")
                    })
                })
        }
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        title = "登录"
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