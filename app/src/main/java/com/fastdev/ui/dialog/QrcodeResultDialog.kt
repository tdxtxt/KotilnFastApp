package com.fastdev.ui.dialog

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.baselib.ui.activity.BaseActivity
import com.baselib.ui.dialog.CenterBaseDialog
import com.baselib.ui.dialog.impl.IBDialog
import com.fastdev.data.repository.DbApiRepository
import com.fastdev.data.response.SourceBean
import com.fastdev.ui.R
import io.reactivex.disposables.Disposable

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/19
 */
class QrcodeResultDialog constructor(val title: String, val activity: BaseActivity, val taskId: String?, val dbApiRepository: DbApiRepository) : CenterBaseDialog(activity) {
    var sourceBean: SourceBean? = null
    var action: ((SourceBean?) -> Unit)? = null
    var tvCode: TextView? = null
    var tvName: TextView? = null
    var disposable: Disposable? = null

    constructor(activity: BaseActivity, taskId: String?, dbApiRepository: DbApiRepository) : this("扫描结果", activity, taskId, dbApiRepository)

    override fun getLayoutId() = R.layout.dialog_qrcode_result

    override fun onCreate(dialog: IBDialog) {
        tvCode = findViewById(R.id.tv_code)
        tvName = findViewById(R.id.tv_name)
        tvCode?.text = sourceBean?.pp_code
        tvName?.text = sourceBean?.pp_name

        if(TextUtils.isEmpty(sourceBean?.pp_name)){
            disposable = dbApiRepository.querySourceByCode(taskId, sourceBean?.pp_code)
                    .compose(activity.bindLifecycle())
                    .subscribe {
                        if(it != null){
                            sourceBean = it
                            tvCode?.text = sourceBean?.pp_code
                            tvName?.text = sourceBean?.pp_name
                        }
                    }
        }


        findViewById<View>(R.id.btn_cancel)?.setOnClickListener {
            dismiss()
        }
        findViewById<View>(R.id.btn_next)?.setOnClickListener {
            action?.invoke(sourceBean)
            dismiss()
        }
    }

    fun showX(sourceBean: SourceBean?, action: (SourceBean?) -> Unit){
        this.sourceBean = sourceBean
        this.action = action
        super.show()
    }
}