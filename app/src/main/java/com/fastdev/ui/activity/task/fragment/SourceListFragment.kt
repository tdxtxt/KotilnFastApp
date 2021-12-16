package com.fastdev.ui.activity.task.fragment

import android.os.Bundle
import com.baselib.ui.fragment.BaseFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fastdev.ui.R
import kotlinx.android.synthetic.main.fragment_source_list.*

class SourceListFragment : BaseFragment() {
    lateinit var adapter: BaseQuickAdapter<String, BaseViewHolder>
    override fun getLayoutId() = R.layout.fragment_source_list

    override fun initUi() {
        adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_source){
            override fun convert(holder: BaseViewHolder, item: String) {
            }

        }
        recyclerView.setAdapter(adapter)

        adapter.setNewInstance(mutableListOf("", "", "", "", ""))
    }

    companion object{
        val _ALL = 1
        val _UN = 2
        val _COMPLETE = 3
        val _OUTSIDE = 4
        val _WANE = 5

        fun getAllInstance() = SourceListFragment().apply { setArguments(Bundle().apply { putInt("type", _ALL) }) }
        fun getUnInstance() = SourceListFragment().apply { setArguments(Bundle().apply { putInt("type", _UN) }) }
        fun getCompleteInstance() = SourceListFragment().apply { setArguments(Bundle().apply { putInt("type", _COMPLETE) }) }
        fun getOutsideInstance() = SourceListFragment().apply { setArguments(Bundle().apply { putInt("type", _OUTSIDE) }) }
        fun getWaneInstance() = SourceListFragment().apply { setArguments(Bundle().apply { putInt("type", _WANE) }) }
    }
}