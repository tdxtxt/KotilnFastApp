package com.fastdev.ui.dialog

import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.baselib.helper.ImageLoaderHelper
import com.baselib.ui.dialog.CenterBaseDialog
import com.baselib.ui.dialog.impl.IBDialog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fastdev.core.MonitorProtocol
import com.fastdev.data.repository.DbApiRepository
import com.fastdev.data.response.SourceBean
import com.fastdev.ui.R
import com.fastdev.ui.activity.task.viewmodel.TaskDetailsViewModel
import top.androidman.SuperButton

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/19
 */
class ScannerDialog constructor(val activity: FragmentActivity, val dbApiRepository: DbApiRepository) : CenterBaseDialog(activity) {
    var recyclerView: RecyclerView? = null
    var tvCount: TextView? = null
    var btnSwicth: SuperButton? = null
    var btnNext: SuperButton? = null
    var ivAnimation: ImageView? = null


    lateinit var viewModel: TaskDetailsViewModel
    lateinit var observer: Observer<MutableList<SourceBean>>
    lateinit var observerSwitcher: Observer<Boolean>
    lateinit var adapter: BaseQuickAdapter<SourceBean, BaseViewHolder>

    override fun getLayoutId() = R.layout.dialog_scanner

    override fun onCreate(dialog: IBDialog) {
        recyclerView = findViewById(R.id.recyclerView)
        btnSwicth = findViewById(R.id.btn_pause)
        btnNext = findViewById(R.id.btn_ok)
        tvCount = findViewById(R.id.tv_count)
        ivAnimation = findViewById(R.id.iv_animation)
        recyclerView?.adapter = object : BaseQuickAdapter<SourceBean, BaseViewHolder>(R.layout.item_dialog_scanner){
            override fun convert(holder: BaseViewHolder, item: SourceBean) {
                holder.setText(R.id.tv_code, item.pp_code)
            }
        }.apply { adapter = this }

        viewModel = TaskDetailsViewModel.get(activity)
        setCancelable(false)
        setCancelListener {
            viewModel.sourceViewModel.removeObserver(observer)
            viewModel.switchScanner.removeObserver(observerSwitcher)
        }
        observer = Observer<MutableList<SourceBean>> {
            adapter.addData(it)
            tvCount?.text = "已扫描${adapter.itemCount}条数据"
            recyclerView?.scrollToPosition(adapter.itemCount - 1)
        }
        observerSwitcher = Observer {
            ImageLoaderHelper.toggleGif(ivAnimation, R.drawable.gif_scan, it)
            if(it){
                btnSwicth?.setText("暂停")
                MonitorProtocol.startReadMonitor(viewModel, dbApiRepository)
            }else{
                btnSwicth?.setText("继续")
                MonitorProtocol.stopReadMonitor()
            }
        }
        viewModel.sourceViewModel.observeForever(observer)
        viewModel.switchScanner.observeForever(observerSwitcher)

        btnSwicth?.setOnClickListener {
            if(MonitorProtocol.isRun){
                viewModel.switchScanner.postValue(false)
            }else{
                viewModel.switchScanner.postValue(true)
            }
        }


        btnNext?.setOnClickListener {
            MonitorProtocol.stopReadMonitor()
            dismiss()
        }

        btnSwicth?.performClick()
    }
}