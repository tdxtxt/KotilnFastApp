package com.fastdev.ui.dialog

import android.text.TextUtils
import android.view.View
import androidx.fragment.app.FragmentActivity
import cn.wzbos.android.widget.linked.LinkedView
import com.baselib.ui.dialog.BottomBaseDialog
import com.baselib.ui.dialog.impl.IBDialog
import com.fastdev.data.response.PlaceBean
import com.fastdev.ui.R
import java.lang.StringBuilder

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/16
 */
class NewSourceFilterDialog constructor(activity: FragmentActivity, val placeList: List<PlaceBean>?) : BottomBaseDialog(activity) {
    var action: ((String) -> Unit)? = null

    private var linkedView: LinkedView? = null

    override fun getLayoutId() = R.layout.dialog_source_filter2

    override fun onCreate(dialog: IBDialog) {
        linkedView = findViewById(R.id.linked)
//        val roomList1 = mutableListOf(PlaceBean("1101", "1栋1楼101房间"), PlaceBean("1102", "1栋1楼102房间"), PlaceBean("1103", "1栋1楼103房间")
//                , PlaceBean("1104", "1栋1楼104房间"), PlaceBean("1105", "1栋1楼105房间"))
//        val roomList2 = mutableListOf(PlaceBean("1201", "1栋2楼201房间"), PlaceBean("1202", "1栋2楼202房间"), PlaceBean("1203", "1栋2楼203房间")
//                , PlaceBean("1204", "204房间"), PlaceBean("1205", "1栋2楼205房间"))
//        val roomList3 = mutableListOf(PlaceBean("2101", "2栋1楼101房间"), PlaceBean("2102", "2栋1楼102房间"), PlaceBean("2103", "2栋1楼103房间")
//                , PlaceBean("2104", "2栋1楼104房间"), PlaceBean("2105", "2栋1楼105房间"))
//        val roomList4 = mutableListOf(PlaceBean("2201", "2栋2楼201房间"), PlaceBean("2202", "2栋2楼202房间"), PlaceBean("2203", "2栋2楼203房间")
//                , PlaceBean("2204", "204房间"), PlaceBean("2205", "2栋2楼205房间"))
//        val foorList1 = mutableListOf(PlaceBean("11", "1栋1楼", roomList1), PlaceBean("12", "1栋2楼", roomList2))
//        val foorList2 = mutableListOf(PlaceBean("21", "2栋1楼", roomList3), PlaceBean("22", "2栋2楼", roomList4))
//        val foorList3 = mutableListOf(PlaceBean("31", "3栋1楼"), PlaceBean("32", "3栋2楼"))
//        val dongList = mutableListOf(PlaceBean("1", "1栋", foorList1), PlaceBean("2", "2栋", foorList2), PlaceBean("1", "3栋", foorList3))

        linkedView?.setLinkedMode(true)
        linkedView?.setDivider(true)
        linkedView?.setOnCreatePickerViewListener { prevView, prevPosition, nextView, nextPosition ->
            nextView.setShowDivider(true)
            when(nextPosition){
                0 -> {
                    nextView.setWidth(0)
                    nextView.setWeight(0.6f)
                    nextView.setMultiSelect(true)
                    nextView.setBackgroundResource(R.color.white_ffffff)
                }
                1 -> {
                    nextView.setWidth(0)
                    nextView.setWeight(1f)
                    nextView.setMultiSelect(true)
                    nextView.setShowIcon(true)
                    nextView.setBackgroundResource(R.color.white_ffffff)
                }
                2 -> {
                    nextView.setWidth(0)
                    nextView.setWeight(1f)
                    nextView.setMultiSelect(true)
                    nextView.setShowIcon(true)
                    nextView.setBackgroundResource(R.color.white_ffffff)
                }
            }
        }
        linkedView?.setOnPickerViewItemClickedListener { pickerView, position, data ->
            when(position){
                0 -> {
                    val optionRoom = linkedView?.getPickerView(2)
                    val optionFoor = linkedView?.getPickerView(1)

                    optionFoor?.refreshPickCount()
                    optionRoom?.refreshPickCount()
                }
                1 -> {//二级列表（楼层）
                    val optionRoom = linkedView?.getPickerView(2)
                    optionRoom?.setAllChecked(data.isCheckedItem)
                }
                2 -> {//三级列表（房间）
                    val optionRoom = linkedView?.getPickerView(2)
                    val optionFoor = linkedView?.getPickerView(1)
                    optionFoor?.setCurrentPostionChecked(optionRoom?.isAllChecked?: true)
                }
            }
        }
        linkedView?.setOnPickedListener { linkView, result ->
            val foorList = mutableListOf<PlaceBean>()
            val roomList = mutableListOf<PlaceBean>()
            for(leveOne in result.selectVaules){
                for(value in leveOne){
                    if(value is PlaceBean){
                        if(!TextUtils.isEmpty(value.parentId) && TextUtils.isEmpty(value.grandparentId)){
                            foorList.add(value)
                        }else if(!TextUtils.isEmpty(value.parentId) && !TextUtils.isEmpty(value.grandparentId)){
                            roomList.add(value)
                        }
                    }
                }
            }

            for(foor in foorList){
                val roomIterator = roomList.iterator()
                while (roomIterator.hasNext()){
                    val room = roomIterator.next()
                    if(foor.id == room.parentId){
                        roomIterator.remove()
                    }
                }
            }

            action?.invoke(createSqlWhere(foorList, roomList))
            dismiss()

        }
        linkedView?.setData(placeList)

        findViewById<View>(R.id.iv_close)?.setOnClickListener {
            dismiss()
        }
    }

    private fun createSqlWhere(selectFoor: List<PlaceBean>?, selectRoom: List<PlaceBean>?): String{
        val resutWhere = StringBuilder("")
        if(selectFoor?.isEmpty() == true && selectRoom?.isEmpty() == true) return resutWhere.toString()
        selectFoor?.forEachIndexed { index, foor ->
            resutWhere.append("( building_code = '${foor.parentId}' AND floor_code = '${foor.id}' )")
            if(index < selectFoor.size - 1) resutWhere.append(" OR ")
        }
        selectRoom?.forEachIndexed { index, room ->
            resutWhere.append("( building_code = '${room.grandparentId}' AND floor_code = '${room.parentId}' AND house_code = '${room.id}' )")
            if(index < selectRoom.size - 1) resutWhere.append(" OR ")
        }
        if(!TextUtils.isEmpty(resutWhere.toString())){
            resutWhere.append(" OR (building_code is null AND floor_code is null AND house_code is null)")
            resutWhere.insert(0, "( ").append(" )")
        }
        return resutWhere.toString()
    }

    fun show(action: (String) -> Unit){
        this.action = action
        super.show()
    }

}