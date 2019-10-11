package com.xily.dmzj2.ui.main

import android.os.Bundle
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseFragment
import com.xily.dmzj2.data.remote.model.LatestBean

class HomeLatestFragment : BaseFragment() {
    override fun initViews(savedInstanceState: Bundle?) {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home_latest
    }

    private fun showLatest(list: List<LatestBean>) {

    }

    companion object {
        fun newInstance() = HomeLatestFragment()
    }
}