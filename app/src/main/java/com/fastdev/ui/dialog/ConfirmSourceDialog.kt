package com.fastdev.ui.dialog

import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.baselib.ui.dialog.CenterBaseDialog
import com.baselib.ui.dialog.impl.IBDialog
import com.baselib.ui.view.other.TextSpanController
import com.fastdev.data.repository.DbApiRepository
import com.fastdev.ui.R
import com.fastdev.ui.activity.task.viewmodel.Quantity

/**
 * 功能描述: 确认资产
 * @author tangdexiang
 * @since 2021/12/19
 */
class ConfirmSourceDialog constructor(activity: FragmentActivity, val quantity: Quantity) : CenterBaseDialog(activity) {
    var action: (() -> Unit)? = null

    override fun getLayoutId() = R.layout.dialog_confirm_source

    override fun onCreate(dialog: IBDialog) {
        findViewById<TextView>(R.id.tv_total)?.text = TextSpanController().append(quantity.all_count).append("\n全部").build()
        findViewById<TextView>(R.id.tv_num_wait)?.text = TextSpanController().append(quantity.wait_count).append("\n待盘").build()
        findViewById<TextView>(R.id.tv_num_finish)?.text = TextSpanController().append(quantity.finish_count).append("\n已盘").build()
        findViewById<TextView>(R.id.tv_num_py)?.text = TextSpanController().append(quantity.py_count).append("\n盘盈").build()
        findViewById<TextView>(R.id.tv_num_pk)?.text = TextSpanController().append(quantity.pk_count).append("\n盘亏").build()

        findViewById<View>(R.id.btn_cancel)?.setOnClickListener {
            dismiss()
        }

        findViewById<View>(R.id.btn_next)?.setOnClickListener {
            action?.invoke()
        }
    }

    fun show(action: () -> Unit){
        this.action = action
        super.show()
    }
}