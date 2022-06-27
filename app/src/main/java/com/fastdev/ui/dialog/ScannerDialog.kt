package com.fastdev.ui.dialog

import android.text.TextUtils
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.baselib.ui.dialog.CenterBaseDialog
import com.baselib.ui.dialog.impl.IBDialog
import com.baselib.ui.view.other.TextSpanController
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fastdev.core.MonitorProtocol
import com.fastdev.data.response.SourceBean
import com.fastdev.ui.R
import com.fastdev.ui.activity.task.viewmodel.Quantity
import com.fastdev.ui.activity.task.viewmodel.TaskDetailsViewModel
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import top.androidman.SuperButton

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/19
 */
class ScannerDialog constructor(val activity: FragmentActivity) : CenterBaseDialog(activity) {
    var recyclerView: RecyclerView? = null
    var tvCount: TextView? = null
    var btnSwicth: SuperButton? = null
    var btnNext: SuperButton? = null
    var ivAnimation: GifImageView? = null
    var tvTotal: TextView? = null
    var tvNumWait: TextView? = null
    var tvNumFinish: TextView? = null

    var action: (() -> Unit)? = null

    lateinit var viewModel: TaskDetailsViewModel
    lateinit var observer: Observer<MutableList<SourceBean>>
    lateinit var observerQuantity: Observer<Pair<String, Quantity>>
    lateinit var observerSwitcher: Observer<Boolean>
    lateinit var adapter: BaseQuickAdapter<SourceBean, BaseViewHolder>

    override fun getLayoutId() = R.layout.dialog_scanner

    override fun onCreate(dialog: IBDialog) {
        setCancelable(false)
        recyclerView = findViewById(R.id.recyclerView)
        btnSwicth = findViewById(R.id.btn_pause)
        btnNext = findViewById(R.id.btn_ok)
        tvCount = findViewById(R.id.tv_count)
        tvNumWait = findViewById(R.id.tv_num_wait)
        tvTotal = findViewById(R.id.tv_total)
        tvNumFinish = findViewById(R.id.tv_num_finish)
        ivAnimation = findViewById(R.id.iv_animation)
        val gifDrwable = ivAnimation?.drawable as GifDrawable?

        recyclerView?.adapter = object : BaseQuickAdapter<SourceBean, BaseViewHolder>(R.layout.item_dialog_scanner){
            override fun convert(holder: BaseViewHolder, item: SourceBean) {
                holder.setText(R.id.tv_code, item.pp_code).setText(R.id.tv_name, item.getNonullName())
            }
        }.apply { adapter = this }

        viewModel = TaskDetailsViewModel.get(activity)
        observer = Observer<MutableList<SourceBean>> {
            viewModel.refreshQuantity.value = ""
            adapter.addData(it)
            tvCount?.text = "已扫描${adapter.itemCount}条数据"
            recyclerView?.scrollToPosition(adapter.itemCount - 1)
        }
        observerSwitcher = Observer { switch ->
            if(switch){
                gifDrwable?.start()
                btnSwicth?.setText("暂停")
            }else{
                gifDrwable?.stop()
                btnSwicth?.setText("继续")
            }
        }
        observerQuantity = Observer {
            if(TextUtils.isEmpty(it.first)){
                tvTotal?.text = TextSpanController().append(it.second.all_cout_by_server).append("\n全部").build()
                tvNumWait?.text = TextSpanController().append(it.second.wait_count).append("\n待盘").build()
                tvNumFinish?.text = TextSpanController().append(it.second.finish_count).append("\n已盘").build()
            }
        }
        viewModel.quantityViewModel.observeForever(observerQuantity)
        viewModel.sourceViewModel.observeForever(observer)
        viewModel.switchScannerViewModel.observeForever(observerSwitcher)

        setCancelListener {
            viewModel.sourceViewModel.removeObserver(observer)
            viewModel.switchScannerViewModel.removeObserver(observerSwitcher)
            viewModel.quantityViewModel.removeObserver(observerQuantity)
        }

        btnSwicth?.setOnClickListener {
            if(MonitorProtocol.isRun){
                viewModel.switchScannerViewModel.postValue(false)
            }else{
                viewModel.switchScannerViewModel.postValue(true)
            }
        }


        btnNext?.setOnClickListener {
            action?.invoke()
            MonitorProtocol.stopReadMonitor()
            dismiss()
        }
        viewModel.refreshQuantity.value = ""
        btnSwicth?.performClick()

    }

    fun show(action: () -> Unit): ScannerDialog{
        this.action = action
        super.show()
        return this
    }
}