package com.fastdev.ui.dialog

import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.baselib.ui.activity.BaseActivity
import com.baselib.ui.dialog.CenterBaseDialog
import com.baselib.ui.dialog.impl.IBDialog
import com.baselib.ui.view.other.TextSpanController
import com.fastdev.data.ResponseBody
import com.fastdev.data.repository.NetApiRepository
import com.fastdev.data.response.SourceBean
import com.fastdev.net.observer.BaseObserver
import com.fastdev.ui.R
import com.fastdev.ui.activity.task.InputSourceActivity
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import io.reactivex.disposables.Disposable

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/19
 */
class QrcodeInputDialog constructor(val activity: BaseActivity, val netApiRepository: NetApiRepository) : CenterBaseDialog(activity) {
    var etContent: EditText? = null
    var tvResult: TextView? = null
    var disposable: Disposable? = null
    var queryCall: ((SourceBean?) -> Unit)? = null

    override fun getLayoutId() = R.layout.dialog_input_qrcode

    override fun onCreate(dialog: IBDialog) {
        etContent = findViewById(R.id.et_content)
        tvResult = findViewById(R.id.tv_result)
        tvResult?.movementMethod = LinkMovementMethod.getInstance()
        tvResult?.text = TextSpanController().append("未查询到结果   ")
                .pushClickSpan(ContextCompat.getColor(activity, R.color.blue_5c95f0)){
                    InputSourceActivity.open(activity){

                    }
                }.append("去手动录入").popSpan().build()
        findViewById<View>(R.id.btn_cancel)?.setOnClickListener {
            dismiss()
        }

        findViewById<View>(R.id.btn_query)?.setOnClickListener {
            val content = etContent?.text.toString()
            querySource(content)
        }
    }

    private fun querySource(code: String?){
        disposable = netApiRepository.querySourceBycode(code)
                    .compose(activity.bindLifecycle())
                    .compose(activity.bindProgress())
                    .subscribeWith(object : BaseObserver<SourceBean>(){
                        override fun onSuccess(response: ResponseBody<SourceBean>?) {
                            queryCall?.invoke(response?.data?.getData())
                        }
                        override fun onFailure(response: ResponseBody<SourceBean>?, errorMsg: String?, e: Throwable?) {
                            tvResult?.visibility = View.VISIBLE
                        }
                    })
    }

    fun showX(queryCall: (SourceBean?) -> Unit): CenterBaseDialog{
        this.queryCall = queryCall
        return super.show()
    }
}