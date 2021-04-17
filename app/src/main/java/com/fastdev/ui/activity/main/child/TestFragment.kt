package com.fastdev.ui.activity.main.child

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.baselib.helper.ImageLoaderHelper
import com.baselib.ui.fragment.BaseFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fastdev.ui.R
import com.huanxin.chat.ChatActivity
import kotlinx.android.synthetic.main.fragment_test.*

class TestFragment : BaseFragment() {
    lateinit var adapter: BaseQuickAdapter<String, BaseViewHolder>
    override fun getLayoutId() = R.layout.fragment_test
    override fun initUi() {
        adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_video){
            override fun convert(holder: BaseViewHolder, item: String) {
                holder.getView<JzvdStd>(R.id.video)
                        .apply {
                            setUp(item , "饺子闭眼睛")
                            ImageLoaderHelper.loadImage(posterImageView, "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640")
                            positionInList = holder.bindingAdapterPosition
                        }
            }
        }
        recyclerView.apply {
            addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener{
                override fun onChildViewDetachedFromWindow(view: View) {
                    val jzvd = view.findViewById<JzvdStd>(R.id.video)
                    if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
                            jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                        if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                            Jzvd.releaseAllVideos()
                        }
                    }
                }
                override fun onChildViewAttachedToWindow(view: View) {
                }
            })
        }.adapter = adapter

        adapter.setNewInstance(mutableListOf("http://8.136.101.204/v/饺子跳.mp4",
                "http://8.136.101.204/v/饺子跳.mp4",
                "http://8.136.101.204/v/饺子受不了.mp4",
                "http://8.136.101.204/v/饺子三位.mp4",
                "http://8.136.101.204/v/饺子起飞.mp4",
                "http://8.136.101.204/v/饺子你听.mp4",
                "http://8.136.101.204/v/饺子可以了.mp4",
                "http://8.136.101.204/v/饺子还小.mp4",
                "http://8.136.101.204/v/饺子高兴.mp4",
                "http://8.136.101.204/v/饺子高冷.mp4",
                "http://8.136.101.204/v/饺子堵住了.mp4",
                "http://8.136.101.204/v/饺子都懂.mp4",
                "http://8.136.101.204/v/饺子打电话.mp4"))
    }
}