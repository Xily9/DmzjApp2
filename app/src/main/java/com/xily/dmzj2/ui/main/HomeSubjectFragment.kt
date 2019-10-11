package com.xily.dmzj2.ui.main

import android.os.Bundle
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseFragment
import com.xily.dmzj2.data.remote.model.SubjectBean

class HomeSubjectFragment : BaseFragment() {
    override fun initViews(savedInstanceState: Bundle?) {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home_subject
    }

    private fun showSubject(list: List<SubjectBean>) {

    }

    companion object {
        fun newInstance() = HomeSubjectFragment()
    }
}