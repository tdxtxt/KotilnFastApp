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
/*
   val STATUS_WAIT = "0" //待盘
   val STATUS_FINISH = "1" //已盘
   val STATUS_PY = "10" //盘盈
   val STATUS_PK = "20" //盘亏
*/
    val indexMap: HashMap<String, Int> = hashMapOf("0" to 0, "1" to 1, "10" to 2, "20" to 3)
    val statusMap: HashMap<Int, String> = hashMapOf(0 to "0", 1 to "1", 2 to "10", 3 to "20")

    override fun getLayoutId() = R.layout.dialog_remark

    override fun onCreate(dialog: IBDialog) {
        spinner = findViewById(R.id.spinner)
        etRemark = findViewById(R.id.et_remark)
        oldStatus = source?.pp_act
        oldRemark = source?.memo
        spinner?.setSelection(indexMap[oldStatus] ?: 0)
        etRemark?.setText(oldRemark?: "")

//        spinner?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                ToastHelper.showToast("onNothingSelected")
//            }
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                ToastHelper.showToast("onItemSelected:$position")
//            }
//        })

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