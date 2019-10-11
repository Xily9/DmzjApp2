package com.xily.dmzj2.ui.main

import android.os.Bundle
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseFragment
import com.xily.dmzj2.data.remote.model.CategoryBean

class HomeCategoryFragment : BaseFragment() {
    override fun initViews(savedInstanceState: Bundle?) {

    }

    override fun getLayoutId(): Int {
        return R.layout.layout_toolbar
    }

    private fun showCategory(list: List<CategoryBean>) {

    }

    companion object {
        fun newInstance() = HomeCategoryFragment()
    }
}