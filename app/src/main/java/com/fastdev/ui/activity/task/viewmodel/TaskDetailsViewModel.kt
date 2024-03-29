package com.fastdev.ui.activity.task.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.fastdev.data.response.PlaceBean
import com.fastdev.data.response.SourceBean

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/22
 */
class TaskDetailsViewModel : ViewModel(){
    var taskId: String = ""
//    var selectFoorList: List<PlaceBean>? = null
//    var selectRoomList: List<PlaceBean>? = null
    var sqlWhere: String = ""

    val sourceViewModel = MutableLiveData<MutableList<SourceBean>>()
    val quantityViewModel = MutableLiveData<Quantity>()
    //扫描器开关
    val switchScanner = MutableLiveData<Boolean>()

    val refreshAll = MutableLiveData<Pair<Option, SourceBean?>>()
    val refreshPK = MutableLiveData<Pair<Option, SourceBean?>>()
    val refreshPY = MutableLiveData<Pair<Option, SourceBean?>>()
    val refreshWait = MutableLiveData<Pair<Option, SourceBean?>>()
    val refreshFinish = MutableLiveData<Pair<Option, SourceBean?>>()
    val refreshQuantity = MutableLiveData<Boolean>()
    val refreshGlobal = MutableLiveData<Boolean>()


    companion object{
        fun get(owner: ViewModelStoreOwner) = ViewModelProvider(owner).get(TaskDetailsViewModel::class.java)
    }
}
enum class Option{
    INSERT, DELETE, UPDATE, RELOAD
}

class Quantity(
    var all_count: Int = 0, //总体数量
    var wait_count: Int = 0, //待盘数量
    var finish_count: Int = 0, //已经盘点数量
    var py_count: Int = 0, //盘盈数量
    var pk_count: Int = 0 //盘亏数量
)