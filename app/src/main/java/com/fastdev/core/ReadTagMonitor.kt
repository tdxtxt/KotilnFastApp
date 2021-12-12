package com.fastdev.core

import android.media.AudioManager
import android.media.SoundPool
import android.os.Looper
import com.baselib.app.ApplicationDelegate
import com.fastdev.app.CustomApp
import com.fastdev.ui.R
import com.seuic.uhf.EPC

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/12
 */
class ReadTagMonitor(looper: Looper) : MonitorProtocol(looper) {
    var soundPool: SoundPool? = null
    var soundID: Int = 0
    var localData: MutableList<EPC>? = null

    override fun task() {
        //读取任务
        val data = mDevice?.getTagIDs()
        if(localData?.size != data?.size) playSound()
        localData = data

    }

    private fun playSound(){
        if(soundPool == null){
            soundPool = SoundPool(3, AudioManager.STREAM_MUSIC, 20)
            soundPool?.load(ApplicationDelegate.context, R.raw.scan, 1)
        }
        soundPool?.play(soundID, 1f, 1f, 0, 0, 1f)
    }
}