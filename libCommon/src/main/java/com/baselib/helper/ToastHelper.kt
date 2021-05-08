package com.baselib.helper

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.annotation.StringRes
import com.baselib.app.ApplicationDelegate
import com.baselib.ui.dialog.child.XToast


@SuppressLint("CheckResult")
object ToastHelper {
    init{
        XToast.Config.get()
                .setAlpha(200)
                .allowQueue(false)
    }

    fun showToast(msg: String?) {
        if (Thread.currentThread() !== Looper.getMainLooper().thread) {//不在主线程
            Handler(Looper.getMainLooper()).post { show(msg) }
        } else {
            show(msg)
        }
    }

    fun showToast(@StringRes res: Int) {
        var msg = ApplicationDelegate.context.getResources()?.getString(res)
        msg = if (TextUtils.isEmpty(msg)) res.toString() + "" else msg

        showToast(msg)
    }

    private fun show(msg: String?) {
        if (TextUtils.isEmpty(msg)) return

        XToast.normal(ApplicationDelegate.context, msg!!).show()
//            val toast = Toast.makeText(DevApp.mContext, null, Toast.LENGTH_SHORT)
//            toast.setText(msg)
//            toast.show()
    }
}