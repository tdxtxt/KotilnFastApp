package com.fastdev.ui.activity.login

import android.app.Activity
import com.baselib.ui.mvp.presenter.BaseMvpPresenter
import com.baselib.ui.mvp.view.BaseMvpView
import com.baselib.ui.mvp.view.activity.CommToolBarMvpActivity
import com.fastdev.ui.R
import com.fastdev.ui.activity.login.presenter.LoginPresenter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.actvity_login_main.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginMainActivity : CommToolBarMvpActivity(), LoginPresenter.BaseMvpImpl {
    @Inject
    lateinit var presenter: LoginPresenter

    override fun createPresenter(): BaseMvpPresenter<*> {
        return presenter
    }

    override fun createMvpView(): BaseMvpView? {
        return this
    }

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