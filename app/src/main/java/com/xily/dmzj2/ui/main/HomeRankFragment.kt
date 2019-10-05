package com.xily.dmzj2.ui.main

import android.os.Bundle
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseFragment
import com.xily.dmzj2.data.remote.model.RankBean
import com.xily.dmzj2.data.remote.model.RankFilterBean

class HomeRankFragment : BaseFragment() {
    override fun initViews(savedInstanceState: Bundle?) {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home_rank
    }

    private fun showRankFilter(list: List<RankFilterBean>) {
    }

    private fun showRank(list: List<RankBean>) {

    }
}