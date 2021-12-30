package com.fastdev.ui.activity.task

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.baselib.helper.LogA
import com.baselib.helper.ToastHelper
import com.baselib.rx.event.RxBus
import com.baselib.ui.mvp.view.activity.CommToolBarMvpActivity
import com.fastdev.core.MonitorProtocol
import com.fastdev.data.event.TaskEventCode
import com.fastdev.data.response.PlaceBean
import com.fastdev.data.response.TaskEntity
import com.fastdev.ui.R
import com.fastdev.ui.activity.qrcode.ScanQrcodeActivity
import com.fastdev.ui.activity.task.fragment.SourceListFragment
import com.fastdev.ui.activity.task.presenter.TaskDetailsPresenter
import com.fastdev.ui.activity.task.viewmodel.Option
import com.fastdev.ui.activity.task.viewmodel.TaskDetailsViewModel
import com.fastdev.ui.adapter.BaseFragmentPagerAdapter
import com.fastdev.ui.dialog.ConfirmSourceDialog
import com.fastdev.ui.dialog.NewSourceFilterDialog
import com.fastdev.ui.dialog.ScannerDialog
import com.seuic.scankey.IKeyEventCallback
import com.seuic.scankey.ScanKeyService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_task_details.*
import javax.inject.Inject

@AndroidEntryPoint
class TaskDetailsActivity : CommToolBarMvpActivity(), TaskDetailsPresenter.BaseMvpImpl {
    @Inject
    lateinit var presenter: TaskDetailsPresenter
    lateinit var viewModel: TaskDetailsViewModel
    private val fragments: MutableList<Pair<String, Fragment>> = mutableListOf()
    private var placeList: List<PlaceBean>? = null
    private lateinit var task: TaskEntity
    private var scanDialogDisplay = false

    private var scanKeyService: ScanKeyService? = try{ ScanKeyService.getInstance() } catch (e: Exception) { null }
    private var keyEventListener: IKeyEventCallback.Stub? = null

    override fun getParams(bundle: Bundle?) {
        val temp: TaskEntity? = bundle?.getParcelable("task")
        if(temp == null){
            ToastHelper.showToast("任务不能为空")
            finish()
        }else{
            task = temp
        }
    }

    override fun createPresenter() = presenter

    override fun createMvpView() = this

    override fun getLayoutResId() = R.layout.activity_task_details

    override fun initUi() {
        viewModel = TaskDetailsViewModel.get(this)
        viewModel.taskId = task.task_id

        if(scanKeyService != null){
            keyEventListener = object : IKeyEventCallback.Stub(){
                override fun onKeyDown(keyCode: Int) {
                    if(250 == keyCode){
                        runOnUiThread{
                            if(scanDialogDisplay){
                                if(viewModel.switchScanner.value == false) viewModel.switchScanner.postValue(true)
                            }else{
                                btn_start.performClick()
                            }
                        }
                    }
                }
                override fun onKeyUp(keyCode: Int) {
                    if(250 == keyCode){
                        viewModel.switchScanner.postValue(false)
                    }
                }
            }
        }

        setTitleBar("盘点任务详情"){
            menuText = "扫一扫"
            onClick { rootView, any ->
                ScanQrcodeActivity.open(this@TaskDetailsActivity, viewModel.taskId){
                    viewModel.refreshGlobal.value = true
                }
            }
        }

        setInterceptBackEvent(false){
            MonitorProtocol.stopReadMonitor()
        }

        listOf(btn_start, btn_end, tv_filter).forEach {
            it.setOnClickListener {
                when(it){
                    btn_start -> {
                        scanDialogDisplay = true
                        ScannerDialog(fragmentActivity).show{
                            //刷新全部
                            viewModel.refreshGlobal.value = true
                            scanDialogDisplay = false
                        }
                    }
                    btn_end -> {
                        presenter.queryStatusQuantity(task.task_id){
                            ConfirmSourceDialog(fragmentActivity, it).show {
                                presenter.commitAllSource(task.task_id)
                            }
                        }
                    }
                    tv_filter -> {
                        if(placeList == null) placeList = presenter.queryPlaceList(viewModel.taskId)
                        NewSourceFilterDialog(fragmentActivity, placeList).show{ sqlWhere ->
                            viewModel.sqlWhere = sqlWhere
                            //刷新全部
                            viewModel.refreshGlobal.value = true
                        }
                    }
                }
            }
        }

        fragments.add(Pair("全部", SourceListFragment.getAllInstance()))
        fragments.add(Pair("待盘", SourceListFragment.getUnInstance()))
        fragments.add(Pair("已盘", SourceListFragment.getCompleteInstance()))
        fragments.add(Pair("盘盈", SourceListFragment.getOutsideInstance()))
        fragments.add(Pair("盘亏", SourceListFragment.getWaneInstance()))

        viewPager.setAdapter(BaseFragmentPagerAdapter(fragments, supportFragmentManager))
        tabLayout.setViewPager(viewPager)
        viewPager.offscreenPageLimit = 5
        viewPager.setCurrentItem(0)
        tabLayout.onPageSelected(0)

        viewModel.quantityViewModel.observe(this, Observer {
            tabLayout.getTitleView(0)?.text = "全部(${it.all_count})"
            tabLayout.getTitleView(1)?.text = "待盘(${it.wait_count})"
            tabLayout.getTitleView(2)?.text = "已盘(${it.finish_count})"
            tabLayout.getTitleView(3)?.text = "盘盈(${it.py_count})"
            tabLayout.getTitleView(4)?.text = "盘亏(${it.pk_count})"
        })

        viewModel.refreshQuantity.observe(this, Observer { isRefresh ->
            if(isRefresh){
                viewModel.refreshQuantity.value = false
                presenter.queryStatusQuantity(task.task_id, viewModel.sqlWhere){
                    viewModel.quantityViewModel.value = it
                }
            }
        })
        viewModel.refreshGlobal.observe(this, Observer { isRefresh ->
            if(isRefresh){
                viewModel.refreshQuantity.value = true
                viewModel.refreshAll.value = Pair(Option.RELOAD, null)
                viewModel.refreshWait.value = Pair(Option.RELOAD, null)
                viewModel.refreshPY.value = Pair(Option.RELOAD, null)
                viewModel.refreshPK.value = Pair(Option.RELOAD, null)
                viewModel.refreshFinish.value = Pair(Option.RELOAD, null)

                viewModel.refreshGlobal.value = false
            }
        })
        updateTask()
        viewModel.refreshGlobal.value = true
        viewModel.switchScanner.observe(this, Observer { switch ->
            if(switch){
                MonitorProtocol.startReadMonitor(viewModel, presenter.dbRepository())
            }else{
                MonitorProtocol.stopReadMonitor()
            }
        })
    }

    private fun updateTask(){
        tv_task_name.text = task.task_name
        tv_task_createname.text = "接口未返"
        tv_task_starttime.text = task.task_time
        tv_task_desc.text = task.task_info
    }

    override fun commitSuc() {
        RxBus.send(TaskEventCode.COMMIT_SUCCESS.setData(taskId))
        finish()
    }

    override fun onResume() {
        super.onResume()
        MonitorProtocol.onResume()
        scanKeyService?.registerCallback(keyEventListener, null)
    }

    override fun onPause() {
        super.onPause()
        MonitorProtocol.onPause()
        scanKeyService?.unregisterCallback(keyEventListener)
    }

    companion object{
        fun open(activity: Activity?, task: TaskEntity?){
            activity?.startActivity(Intent(activity, TaskDetailsActivity::class.java).putExtra("task", task))
        }
    }

}
