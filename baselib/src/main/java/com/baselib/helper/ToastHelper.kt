package com.baselib.helper

import android.os.Handler
import android.os.Looper
import android.support.annotation.StringRes
import android.text.TextUtils
import android.widget.Toast
import com.baselib.app.DevApp

class ToastHelper {
    companion object {
        open fun showToast(msg: String?) {
            if (Thread.currentThread() !== Looper.getMainLooper().thread) {//不在主线程
                Handler(Looper.getMainLooper()).post { show(msg) }
            } else {
                show(msg)
            }
        }

        open fun showToast(@StringRes res: Int) {
            if (DevApp.mContext == null) return
            var msg = DevApp.mContext?.getResources()?.getString(res)
            msg = if (TextUtils.isEmpty(msg)) res.toString() + "" else msg

            showToast(msg)
        }

        private fun show(msg: String?) {
            if (DevApp.mContext == null) return
            if (TextUtils.isEmpty(msg)) return
            val toast = Toast.makeText(DevApp.mContext, null, Toast.LENGTH_SHORT)
            toast.setText(msg)
            toast.show()
        }
    }
}