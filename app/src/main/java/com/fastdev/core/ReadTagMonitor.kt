package com.fastdev.core

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Looper
import com.baselib.app.ApplicationDelegate
import com.baselib.helper.LogA
import com.fastdev.data.repository.DbApiRepository
import com.fastdev.data.response.SourceBean
import com.fastdev.ui.R
import com.fastdev.ui.activity.task.viewmodel.TaskDetailsViewModel

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/12
 */
class ReadTagMonitor(looper: Looper?, var viewModel: TaskDetailsViewModel, var dbApiRepository: DbApiRepository) : MonitorProtocol(looper) {
    private var soundPool: SoundPool? = null
    private val localData: HashMap<String, Boolean> = HashMap()

    override fun task() {
        //读取任务
        val data = UHFSdk.read()
        val diffData = data?.filter {
            (localData[it.getId()] != true).apply {
                if(this) localData[it.getId()] = true
            }
        }?.map {
            SourceBean().apply {
                pp_code = it.getId()
                dbApiRepository.syncSaveOrUpdate(viewModel.taskId, this)
            }
        }
        LogA.i("NewSource： $diffData")
        if((diffData?.size?:0) > 0){
            playSound()
            //将数据传递到主线程之中
            viewModel.sourceViewModel.postValue(diffData?.toMutableList())
        }
    }

    override fun start() {
        super.start()
        UHFSdk.start()
        viewModel.switchScannerViewModel.postValue(true)
    }

    override fun close() {
        UHFSdk.stop()
        localData.clear()
        viewModel.switchScannerViewModel.postValue(false)
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