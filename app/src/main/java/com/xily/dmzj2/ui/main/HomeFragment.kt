package com.xily.dmzj2.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseFragment
import com.xily.dmzj2.ui.rank.HomeRankFragment
import com.xily.dmzj2.ui.search.SearchActivity
import com.xily.dmzj2.utils.startActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        initViewPager()
    }

    private fun initViewPager() {
        val titles = arrayOf("推荐", "更新", "分类", "排行", "专题")
        viewPager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> HomeIndexFragment.newInstance()
                    1 -> HomeLatestFragment.newInstance()
                    2 -> HomeCategoryFragment.newInstance()
                    3 -> HomeRankFragment.newInstance()
                    4 -> HomeSubjectFragment.newInstance()
                    else -> HomeIndexFragment.newInstance()
                }
            }

            override fun getCount(): Int {
                return titles.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return titles[position]
            }
        }
        tab.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> startActivity<SearchActivity>()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

}