package com.fastdev.ui.activity.qrcode

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.fragment.app.FragmentActivity
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.baselib.app.ApplicationDelegate
import com.baselib.helper.RequestPermissionHelper
import com.baselib.helper.ScreenHelper
import com.baselib.helper.StatusBarHelper
import com.baselib.ui.activity.BaseActivity
import com.fastdev.ui.R
import kotlinx.android.synthetic.main.activity_scan_qrcode.*

/**
 * 公用类：二维码扫描
 */
class ScanQrcodeActivity : BaseActivity(), QRCodeView.Delegate {
    var soundPool: SoundPool? = null

    /**
     * 扫描结果处理
     */
    override fun onScanQRCodeSuccess(result: String?) {
        hideProgressBar()
//        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
//        vibrator.vibrate(200)
        playSound{
            setResult(Activity.RESULT_OK, Intent().putExtra("result", result))
            finish()
        }
    }


    override fun getLayoutResId() = R.layout.activity_scan_qrcode

    override fun initUi() {
        StatusBarHelper.transparentStatusBar(this)
        setInterceptBackEvent(false){
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        zxingview.apply {
            setDelegate(this@ScanQrcodeActivity)
            scanBoxView.apply {
                cornerColor = Color.TRANSPARENT
                postDelayed({
                    rectWidth = ScreenHelper.getScreenWidth(fragmentActivity)
                    rectHeight = ScreenHelper.getScreenHeight(fragmentActivity) - fragmentActivity.view_menu.height
                }, 200)
            }
        }
        listOf(view_flashlight).forEach {
            it.setOnClickListener {
                when(it){
                    view_flashlight -> if (zxingview.isFlashlight) {
                        zxingview.closeFlashlight()
                        tv_flashlight.text = "轻触点亮"
                        iv_flashlight.setImageResource(R.mipmap.icon_flashlight_off)
                        view_flashlight.visibility = View.GONE
                    } else {
                        zxingview.openFlashlight()
                        tv_flashlight.text = "轻触关闭"
                        iv_flashlight.setImageResource(R.mipmap.icon_flashlight_on)
                        view_flashlight.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //打开后置摄像头开始预览，但是并未开始识别
        zxingview.startCamera()
        zxingview.startSpot()
    }

    override fun onResume() {
        super.onResume()
        //显示扫描框，并开始识别
        zxingview.startSpotAndShowRect()
    }

    override fun onPause() {
        super.onPause()
        zxingview.stopSpot()
    }

    override fun onStop() {
        zxingview.stopCamera()
        super.onStop()
    }


    override fun onDestroy() {
        zxingview.onDestroy()
        soundPool?.release()
        super.onDestroy()
    }

    private fun playSound(playCompleteListener: () -> Unit){
        if(soundPool == null){
            if(Build.VERSION.SDK_INT > 21){
                val builder = SoundPool.Builder()
                builder.setMaxStreams(3)
                val attrBuilder = AudioAttributes.Builder()
                attrBuilder.setLegacyStreamType(AudioManager.STREAM_SYSTEM)
                builder.setAudioAttributes(attrBuilder.build())
                soundPool = builder.build()
            }else{
                soundPool = SoundPool(3, AudioManager.STREAM_MUSIC, 20)
            }
            soundPool?.load(ApplicationDelegate.context, R.raw.scan, 1)
        }
        soundPool?.setOnLoadCompleteListener(object : SoundPool.OnLoadCompleteListener {
            override fun onLoadComplete(soundPool: SoundPool?, sampleId: Int, status: Int) {
                soundPool?.play(sampleId, 1f, 1f, 0, 0, 1f)
                playCompleteListener.invoke()
            }
        })
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        if(isDark){
            if(view_flashlight.visibility == View.VISIBLE) return
            view_flashlight.visibility = View.VISIBLE
            flashingAnimation(iv_flashlight)
        }else{
            iv_flashlight.clearAnimation()
            if(zxingview.isFlashlight){
                view_flashlight.visibility = View.VISIBLE
            }else{
                view_flashlight.visibility = View.GONE
            }
        }
    }

    private fun flashingAnimation(view: View) {
        view.apply {
            startAnimation(AlphaAnimation(0.1f, 1.0f).apply {
                duration = 1000
                repeatCount = Animation.INFINITE
                repeatMode = Animation.RESTART
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(p0: Animation?) {}
                    override fun onAnimationEnd(p0: Animation?) {}
                    override fun onAnimationStart(p0: Animation?) {}
                })
            })
        }
    }

    override fun onScanQRCodeOpenCameraError() {
         RequestPermissionHelper.requestCameraPermission(this){
             grantedPermission {
                 zxingview.startCamera()
                 zxingview.startSpotAndShowRect()
             }
             deniedPermission {
                 setResult(Activity.RESULT_CANCELED)
                 finish()
             }
         }
    }

    companion object{
        fun open(activity: FragmentActivity?, listener: ((result: String?) -> Unit)? = null){
//            Timber.i("open: time = ${System.currentTimeMillis()}")
            RequestPermissionHelper.requestCameraPermission(activity){
                grantedPermission {
                    activity?.startActivityForResult(ScanQrcodeActivity::class.java, null){
                        onActivityResult { requestCode, resultCode, data ->
                            if(resultCode == Activity.RESULT_OK) listener?.invoke(data?.getStringExtra("result"))
                        }
                    }
                }
            }
        }
    }

}