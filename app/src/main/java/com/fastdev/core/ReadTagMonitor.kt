package com.fastdev.core

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Looper
import com.baselib.app.ApplicationDelegate
import com.baselib.helper.LogA
import com.fastdev.app.CustomApp
import com.fastdev.ui.R
import com.seuic.uhf.EPC

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/12
 */
class ReadTagMonitor(looper: Looper?) : MonitorProtocol(looper) {
    var soundPool: SoundPool? = null
    var localData: MutableList<EPC>? = null

    override fun task() {
        //读取任务
        val data = UHFSdk.read()
        if(localData?.size != data?.size) playSound()
        localData = data
        LogA.i("readTag： $data")

        //将数据传递到主线程之中
    }

    override fun start() {
        super.start()
        UHFSdk.start()
    }

    override fun close() {
        UHFSdk.stop()
        super.close()
    }

    private fun playSound(){
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
            }
        })
    }
}