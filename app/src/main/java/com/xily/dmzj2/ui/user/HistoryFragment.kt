package com.xily.dmzj2.ui.user

import android.os.Bundle
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseFragment
import com.xily.dmzj2.data.remote.model.HistoryBean

class HistoryFragment : BaseFragment() {
    override fun initViews(savedInstanceState: Bundle?) {

    }

    private fun showHistory(list: List<HistoryBean>) {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_history
    }

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }
}