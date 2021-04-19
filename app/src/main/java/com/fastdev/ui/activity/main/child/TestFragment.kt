package com.fastdev.ui.activity.main.child

import android.content.pm.ActivityInfo
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.baselib.helper.ImageLoaderHelper
import com.baselib.ui.fragment.BaseFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fastdev.ui.R
import com.yc.kernel.impl.exo.ExoMediaPlayer
import com.yc.kernel.impl.exo.ExoPlayerFactory
import com.yc.video.config.ConstantKeys
import com.yc.video.config.VideoInfoBean
import com.yc.video.config.VideoPlayerConfig
import com.yc.video.player.SimpleStateListener
import com.yc.video.player.VideoPlayer
import com.yc.video.player.VideoViewManager
import com.yc.video.tool.PlayerUtils
import com.yc.video.ui.view.BasisVideoController
import com.yc.video.ui.view.CustomPrepareView
import kotlinx.android.synthetic.main.fragment_test.*


class TestFragment : BaseFragment() {
    lateinit var adapter: BaseQuickAdapter<VideoInfoBean, BaseViewHolder>
    override fun getLayoutId() = R.layout.fragment_test
    override fun initUi() {
        //播放器配置，注意：此为全局配置，例如下面就是配置ijk内核播放器
        VideoViewManager.setConfig(VideoPlayerConfig.newBuilder()
                .setLogEnabled(true)//调试的时候请打开日志，方便排错
                .setPlayerFactory(ExoPlayerFactory.create())
                .build())

        adapter = object : BaseQuickAdapter<VideoInfoBean, BaseViewHolder>(R.layout.item_video){
            override fun convert(holder: BaseViewHolder, item: VideoInfoBean) {
                holder.itemView.tag = holder
                ImageLoaderHelper.loadImage(holder.getView<CustomPrepareView>(R.id.video_player).thumb, item.cover)
            }
        }
        recyclerView.apply {
            addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener{
                override fun onChildViewDetachedFromWindow(view: View) {
                    val playerContainer: FrameLayout = view.findViewById(R.id.video_player)
                    val v = playerContainer.getChildAt(0)
                    if (v != null && v === mVideoView && !mVideoView!!.isFullScreen) {
                        releaseVideoView()
                    }
                }
                override fun onChildViewAttachedToWindow(view: View) {}
            })
        }.adapter = adapter

        adapter.setOnItemClickListener { adapter, view, position ->
            startPlay(position)
        }
        adapter.setNewInstance(mutableListOf(
                VideoInfoBean("title", "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2351043261,718687726&fm=26&gp=0.jpg", "http://8.136.101.204/v/饺子跳.mp4"),
                VideoInfoBean("title", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0125f15db641a1a801209e1fb16d96.jpg%403000w_1l_2o_100sh.jpg&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621413552&t=525f911a20c18765c9a50f3fba401d6e", "http://8.136.101.204/v/饺子受不了.mp4"),
                VideoInfoBean("title", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fstatic.smxs.com%2Fuploads%2Fallimg%2F190206%2F22-1Z206234110X4.jpg&refer=http%3A%2F%2Fstatic.smxs.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621413552&t=af27a99f8994fd233706d1560c01350d", "http://8.136.101.204/v/饺子三位.mp4"),
                VideoInfoBean("title", "", "http://8.136.101.204/v/饺子起飞.mp4"),
                VideoInfoBean("title", "", "http://8.136.101.204/v/饺子你听.mp4"),
                VideoInfoBean("title", "", "http://8.136.101.204/v/饺子还小.mp4"),
                VideoInfoBean("title", "", "http://8.136.101.204/v/饺子高兴.mp4"),
                VideoInfoBean("title", "", "http://8.136.101.204/v/饺子高冷.mp4"),
                VideoInfoBean("title", "", "http://8.136.101.204/v/饺子堵住了.mp4"),
                VideoInfoBean("title", "", "http://8.136.101.204/v/饺子都懂.mp4"),
                VideoInfoBean("title", "", "http://8.136.101.204/v/饺子打电话.mp4"),
                VideoInfoBean("title", "", "http://8.136.101.204/v/饺子受不了.mp4")))

        videoInit()
    }

    private var mVideoView: VideoPlayer<ExoMediaPlayer>? = null
    private var mController: BasisVideoController? = null
    private var mLastPos: Int = -1
    private var mCurPos = 0
    private fun videoInit(){
        if(fragmentActivity == null) return
        mVideoView = VideoPlayer(fragmentActivity!!)
        mVideoView?.setOnStateChangeListener(object : SimpleStateListener(){
            override fun onPlayerStateChanged(playerState: Int) {
                if(playerState == ConstantKeys.CurrentState.STATE_IDLE){
                    PlayerUtils.removeViewFormParent(mVideoView)
                    mLastPos = mCurPos
                    mCurPos = -1
                }
            }
        })
        mController = BasisVideoController(fragmentActivity!!)
        mVideoView?.setController(mController)
    }

    override fun onResume() {
        super.onResume()
        if (mLastPos == -1)
            return
        //恢复上次播放的位置
        startPlay(mLastPos)
    }

    override fun onPause() {
        super.onPause()
        releaseVideoView()
    }

    /**
     * 开始播放
     * @param position 列表位置
     */
    private fun startPlay(position: Int) {
        if (mCurPos == position) return
        if (mCurPos != -1) {
            releaseVideoView()
        }
        val videoBean = adapter.getItem(position)
        mVideoView?.setUrl(videoBean.videoUrl)

        val itemView = recyclerView.layoutManager?.findViewByPosition(position)

        val viewHolder = itemView?.tag as BaseViewHolder?
        //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true。
        mController?.addControlComponent(viewHolder?.getView<CustomPrepareView>(R.id.video_player), true)
        PlayerUtils.removeViewFormParent(mVideoView)
        viewHolder?.getView<FrameLayout>(R.id.player_container)?.addView(mVideoView, 0)
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        VideoViewManager.instance().add(mVideoView, "list")
        mVideoView?.start()
        mCurPos = position
    }

    private fun releaseVideoView() {
        if (mVideoView?.isFullScreen == true) {
            mVideoView?.stopFullScreen()
        }
        mVideoView?.release()
        if (activity?.requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        mCurPos = -1
    }

}