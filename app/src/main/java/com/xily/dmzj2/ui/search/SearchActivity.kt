package com.xily.dmzj2.ui.search

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import co.lujun.androidtagview.TagView
import com.xily.dmzj2.R
import com.xily.dmzj2.base.BaseActivity
import com.xily.dmzj2.ui.info.InfoActivity
import com.xily.dmzj2.utils.hideSoftInput
import com.xily.dmzj2.utils.toastError
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream

class SearchActivity : BaseActivity() {

    private lateinit var adapter: SearchAdapter
    private val searchViewModel: SearchViewModel by viewModel()

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolBar()
        initSearchView()
        initRecyclerView()
        clear.setOnClickListener {
            searchViewModel.clearSearchHistory()
            //tagGroup.removeAllTags()
        }

        tagGroupHot.setOnTagClickListener(object : TagView.OnTagClickListener {
            override fun onSelectedTagDrag(position: Int, text: String?) {
            }

            override fun onTagLongClick(position: Int, text: String?) {
            }

            override fun onTagCrossClick(position: Int) {
            }

            override fun onTagClick(position: Int, text: String?) {
                et_search.setText(text)
                iv_search.performClick()
            }

        })
        tagGroup.setOnTagClickListener(object : TagView.OnTagClickListener {
            override fun onSelectedTagDrag(position: Int, text: String?) {
            }

            override fun onTagLongClick(position: Int, text: String?) {
            }

            override fun onTagCrossClick(position: Int) {
            }

            override fun onTagClick(position: Int, text: String?) {
                et_search.setText(text)
                iv_search.performClick()
            }
        })
        searchViewModel.getSearchHistory().observe(this, Observer {
            tagGroup.tags = it
        })
        searchViewModel.getSearchHot().observe(this, Observer {
            tagGroupHot.tags = it.map {
                it.name
            }
        })
    }

    private fun initRecyclerView() {
        recycle.layoutManager = LinearLayoutManager(this)
        adapter = SearchAdapter()
        recycle.adapter = adapter
        adapter.setOnItemClickListener { position, binding ->
            val intent = Intent(this, InfoActivity::class.java)
            val bitmap = (binding.ivCover.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, baos)
            val bytes = baos.toByteArray()
            intent.putExtra("bitmap", bytes)
            intent.putExtra("id", adapter.currentList.get(position).id)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    binding.ivCover,
                    "image"
                ).toBundle()
            )
        }
    }

    private fun initSearchView() {
        et_search.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        iv_back.setOnClickListener { finish() }
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.isNotEmpty()) {
                        iv_close.visibility = View.VISIBLE
                    } else {
                        iv_close.visibility = View.GONE
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        et_search.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                empty.visibility = View.VISIBLE
                recycle.visibility = View.GONE
                if (et_search.text.isNotEmpty()) {
                    iv_close.visibility = View.VISIBLE
                } else {
                    iv_close.visibility = View.GONE
                }
            }
        }
        iv_close.setOnClickListener {
            et_search.setText("")
        }
        et_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                iv_search.performClick()
            }
            false
        }
        iv_search.setOnClickListener {
            val text = et_search.text.toString()
            if (text.isNotEmpty()) {
                adapter.submitList(arrayListOf())
                searchViewModel.addSearchHistory(text)
                iv_close.visibility = View.GONE
                empty.visibility = View.GONE
                recycle.visibility = View.VISIBLE
                hideSoftInput()
                et_search.clearFocus()
                progress.visibility = View.VISIBLE
                searchViewModel.search(text, "0").observe(this, Observer {
                    if (it != null) {
                        adapter.submitList(it)
                    } else {
                        toastError("搜索失败!")
                    }
                    progress.visibility = View.GONE
                })
            }
        }
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
    }

}