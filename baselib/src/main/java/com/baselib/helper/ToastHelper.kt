package com.baselib.helper

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.StringRes
import com.baselib.app.DevApp
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
        if (DevApp.mContext == null) return
        var msg = DevApp.mContext?.getResources()?.getString(res)
        msg = if (TextUtils.isEmpty(msg)) res.toString() + "" else msg

        showToast(msg)
    }

    private fun show(msg: String?) {
        if (DevApp.mContext == null) return
        if (TextUtils.isEmpty(msg)) return

        XToast.normal(DevApp.mContext!!, msg!!).show()
//            val toast = Toast.makeText(DevApp.mContext, null, Toast.LENGTH_SHORT)
//            toast.setText(msg)
//            toast.show()
    }
}