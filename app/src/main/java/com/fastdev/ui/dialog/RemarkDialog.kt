package com.fastdev.ui.dialog

import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.FragmentActivity
import com.baselib.helper.ToastHelper
import com.baselib.ui.dialog.BottomBaseDialog
import com.baselib.ui.dialog.impl.IBDialog
import com.fastdev.data.response.SourceBean
import com.fastdev.ui.R

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/22
 */
class RemarkDialog constructor(activity: FragmentActivity) : BottomBaseDialog(activity) {
    var source: SourceBean? = null
    var changeListener: ((source: SourceBean?) -> Unit)? = null

    var spinner: Spinner? = null
    var etRemark: EditText? = null

    var oldStatus: String? = null
    var oldRemark: String? = null

    val indexMap: HashMap<String, Int> = hashMapOf(
        SourceBean.STATUS_WAIT to 0,
        SourceBean.STATUS_PY to 1,
        SourceBean.STATUS_PK to 2,
        SourceBean.STATUS_FINISH to 3)

    val statusMap: HashMap<Int, String> = hashMapOf(
            0 to SourceBean.STATUS_WAIT,
            1 to SourceBean.STATUS_PY,
            2 to SourceBean.STATUS_PK,
            3 to SourceBean.STATUS_FINISH)

    override fun getLayoutId() = R.layout.dialog_remark

    override fun onCreate(dialog: IBDialog) {
        spinner = findViewById(R.id.spinner)
        etRemark = findViewById(R.id.et_remark)
        oldStatus = source?.pp_act
        oldRemark = source?.memo
        spinner?.setSelection(indexMap[oldStatus] ?: 0)
        etRemark?.setText(oldRemark?: "")
        etRemark?.setSelection(oldRemark?.length?:0)

        findViewById<View>(R.id.btn_next)?.setOnClickListener {
            source?.apply {
                pp_act = statusMap[spinner?.selectedItemPosition]?: pp_act
                memo = etRemark?.text.toString()
            }
            if((oldRemark?:"") == (source?.memo?:"") && (oldStatus?:"") == (source?.pp_act?:"")){
                //没有改变
            }else{
                changeListener?.invoke(source)
            }
            dismiss()
        }
    }

    fun show(source: SourceBean, changeListener: (source: SourceBean?) -> Unit){
        this.source = source
        this.changeListener = changeListener
        super.show()
    }

}