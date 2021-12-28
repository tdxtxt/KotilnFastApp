package com.fastdev.ui.activity.task

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.baselib.helper.HashMapParams
import com.baselib.helper.ToastHelper
import com.baselib.ui.activity.CommToolBarActivity
import com.baselib.ui.mvp.view.activity.CommToolBarMvpActivity
import com.fastdev.data.response.SourceBean
import com.fastdev.ui.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_source_input.*
import javax.inject.Inject

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/22
 */
@AndroidEntryPoint
class InputSourceActivity : CommToolBarActivity() {

    var taskId: String? = ""
    var code: String? = ""

    override fun getParams(bundle: Bundle?) {
        taskId = bundle?.getString("taskId")
        code = bundle?.getString("code")
    }

    override fun getLayoutResId() = R.layout.activity_source_input

    override fun initUi() {
        setTitleBar("资产录入")

        et_code.setText(code)

        findViewById<View>(R.id.btn_next)?.setOnClickListener {
            val code = et_code.text.toString()
            val name = et_name.text.toString()
            val type = et_type.text.toString()
            val address = et_address.text.toString()
            val model = et_model.text.toString()
            val remark = et_remark.text.toString()

            if(TextUtils.isEmpty(code)){
                ToastHelper.showToast("资产编号不能为空")
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(name)){
                ToastHelper.showToast("资产名称不能为空")
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(type)){
                ToastHelper.showToast("资产类型不能为空")
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(address)){
                ToastHelper.showToast("存放地址不能为空")
                return@setOnClickListener
            }

            val source = SourceBean(pp_code =  code, pp_name = name, pp_type =  type, pp_model = model, pp_addr = address, memo = remark)

            setResult(Activity.RESULT_OK, intent.putExtra("sourceBean", source))
            finish()
        }
    }

    companion object{
        fun open(activity: Activity?, taskId: String?, code: String?, action: (SourceBean?) -> Unit){
            activity?.startActivityForResult(InputSourceActivity::class.java, HashMapParams().add("taskId", taskId?: "")
                    .add("code", code?: "")) {
                onActivityResult { requestCode, resultCode, data ->
                    action.invoke(data?.getParcelableExtra("sourceBean"))
                }
            }
        }
    }


}