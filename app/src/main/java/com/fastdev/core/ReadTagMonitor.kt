package com.fastdev.core

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Looper
import com.baselib.app.ApplicationDelegate
import com.baselib.helper.LogA
import com.baselib.helper.ToastHelper
import com.fastdev.data.repository.DbApiRepository
import com.fastdev.data.repository.NetApiRepository
import com.fastdev.data.response.SourceBean
import com.fastdev.ui.R
import com.fastdev.ui.activity.task.viewmodel.TaskDetailsViewModel

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/12
 */
class ReadTagMonitor(looper: Looper?, var viewModel: TaskDetailsViewModel, var dbApiRepository: DbApiRepository, var netApiRepository: NetApiRepository) : MonitorProtocol(looper) {
    private var soundPool: SoundPool? = null
//    private val localData: HashMap<String, Boolean> = HashMap()

    override fun task() {
        //读取任务
        val data = UHFSdk.read()
        data?.filter { it.getId()?.startsWith("f")?: false }

        val diffData = data?.filter {
            (viewModel.localData[it.getId()] != true && !it.getId().startsWith("E")).apply {
                if(this) viewModel.localData[it.getId()] = true
            }
        }?.map {
            LogA.i("【扫描卡片信息】： $it")
            if(it.getId().startsWith("f")){//是房间
                val roomSource = netApiRepository.querySourceByRoom(it.getId())
                if(roomSource?.isSuccess() == true && roomSource.data?.isEmpty() == false){
                    val sourceList = roomSource.data.getListData()
                    dbApiRepository.syncSaveOrUpdate(viewModel.taskId, sourceList)
                }else{
                    viewModel.localData.remove(it.getId())
                }
                roomSource?.data?.getListData()
            }else{
                val sourceBean = SourceBean(pp_code = it.getId())
                listOf(dbApiRepository.syncSaveOrUpdate(viewModel.taskId, sourceBean)?: sourceBean)
            }
        }
//        LogA.i("NewSource： $diffData")
        if((diffData?.size?:0) > 0){
            costTime = 0
            val sourceList = mutableListOf<SourceBean>()
            diffData?.forEach {
                it?.forEach {
                   sourceList.add(it)
                }
            }
            LogA.i("【显示卡片信息】： $sourceList")
            playSound()
            //将数据传递到主线程之中
            viewModel.sourceViewModel.postValue(sourceList)
        }else{
            if(costTime >= 4) ToastHelper.showToast("未扫描到新的资产信息")
        }
    }

    override fun start() {
        super.start()
        UHFSdk.start()
        viewModel.switchScannerViewModel.postValue(true)
    }

    override fun close() {
        UHFSdk.stop()
//        localData.clear()
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