package com.fastdev.ui.activity.login

import android.app.Activity
import com.baselib.ui.activity.CommToolBarActivity
import com.fastdev.ui.R
import kotlinx.android.synthetic.main.actvity_login_main.*

class LoginMainActivity : CommToolBarActivity() {
    override fun getLayoutResId() = R.layout.actvity_login_main

    override fun initUi() {
        setInterceptBackEvent(false){
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        btn_next.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object{
        fun open(activity: Activity?, loginSuccess: () -> Unit, loginCancel: () -> Unit){
            activity?.startActivityForResult(LoginMainActivity::class.java, null){
                onActivityResult { requestCode, resultCode, data ->
                    if(resultCode == Activity.RESULT_OK){
                        loginSuccess.invoke()
                    }else{
                        loginCancel.invoke()
                    }
                }
            }
        }
    }
}