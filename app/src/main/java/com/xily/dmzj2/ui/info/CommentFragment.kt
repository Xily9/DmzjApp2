package com.xily.dmzj2.ui.info

import android.os.Bundle
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseFragment
import com.xily.dmzj2.utils.getAttrColor
import kotlinx.android.synthetic.main.layout_info_pager_comment.*
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel

/**
 * Created by Xily on 2019/10/10.
 */
class CommentFragment : BaseFragment() {
    private lateinit var infoViewModel: InfoViewModel
    override fun getLayoutId(): Int {
        return R.layout.layout_info_pager_comment
    }

    override fun initViews(savedInstanceState: Bundle?) {
        infoViewModel = getSharedViewModel()
        swipe.setColorSchemeColors(getAttrColor(R.attr.colorAccent))
        swipe.setOnRefreshListener {
            loadData()
        }
        loadData()
    }

    private fun loadData() {

    }

    companion object {
        fun newInstance() = CommentFragment()
    }
}