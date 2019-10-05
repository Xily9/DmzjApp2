package com.xily.dmzj2.ui.common

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseActivity
import kotlinx.android.synthetic.main.activity_browser.*
import kotlinx.android.synthetic.main.activity_info.*

class BrowserActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_browser
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViews(savedInstanceState: Bundle?) {
        initToolBar()
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.useWideViewPort = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.loadWithOverviewMode = true
        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = MyWebChromeClient()
        val url = intent.getStringExtra("url")
        webView.loadUrl(url)
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    inner class MyWebChromeClient : WebChromeClient() {
        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            setTitle(title)
        }

        override fun onJsAlert(
            view: WebView,
            url: String,
            message: String,
            result: JsResult
        ): Boolean {
            AlertDialog.Builder(this@BrowserActivity)
                .setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton("确定") { _, _ -> result.confirm() }
                .setCancelable(false)
                .show()
            return true
        }
    }

    inner class MyWebViewClient : WebViewClient() {

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progress.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progress.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.browser_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed() // back button
            R.id.close -> finish()
        }
        return true
    }
}
