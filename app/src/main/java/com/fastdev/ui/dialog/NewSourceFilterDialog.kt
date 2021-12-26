package com.fastdev.ui.dialog

import android.view.View
import androidx.fragment.app.FragmentActivity
import cn.wzbos.android.widget.linked.LinkedView
import com.baselib.helper.ToastHelper
import com.baselib.ui.dialog.BottomBaseDialog
import com.baselib.ui.dialog.impl.IBDialog
import com.fastdev.data.response.PlaceBean
import com.fastdev.ui.R

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/16
 */
class NewSourceFilterDialog constructor(activity: FragmentActivity) : BottomBaseDialog(activity) {

    private var linkedView: LinkedView? = null

    override fun getLayoutId() = R.layout.dialog_source_filter2

    override fun onCreate(dialog: IBDialog) {
        linkedView = findViewById(R.id.linked)
        val roomList1 = mutableListOf(PlaceBean("1101", "1栋1楼101房间"), PlaceBean("1102", "1栋1楼102房间"), PlaceBean("1103", "1栋1楼103房间")
                , PlaceBean("1104", "1栋1楼104房间"), PlaceBean("1105", "1栋1楼105房间"))

        val roomList2 = mutableListOf(PlaceBean("1201", "1栋2楼201房间"), PlaceBean("1202", "1栋2楼202房间"), PlaceBean("1203", "1栋2楼203房间")
                , PlaceBean("1204", "204房间"), PlaceBean("1205", "1栋2楼205房间"))

        val roomList3 = mutableListOf(PlaceBean("2101", "2栋1楼101房间"), PlaceBean("2102", "2栋1楼102房间"), PlaceBean("2103", "2栋1楼103房间")
                , PlaceBean("2104", "2栋1楼104房间"), PlaceBean("2105", "2栋1楼105房间"))

        val roomList4 = mutableListOf(PlaceBean("2201", "2栋2楼201房间"), PlaceBean("2202", "2栋2楼202房间"), PlaceBean("2203", "2栋2楼203房间")
                , PlaceBean("2204", "204房间"), PlaceBean("2205", "2栋2楼205房间"))

        val foorList1 = mutableListOf(PlaceBean("11", "1栋1楼", roomList1), PlaceBean("12", "1栋2楼", roomList2))

        val foorList2 = mutableListOf(PlaceBean("21", "2栋1楼", roomList3), PlaceBean("22", "2栋2楼", roomList4))

        val foorList3 = mutableListOf(PlaceBean("31", "3栋1楼"), PlaceBean("32", "3栋2楼"))

        val dongList = mutableListOf(PlaceBean("1", "1栋", foorList1), PlaceBean("2", "2栋", foorList2), PlaceBean("1", "3栋", foorList3))

        linkedView?.setLinkedMode(true)
        linkedView?.setDivider(true)
        linkedView?.setOnCreatePickerViewListener { prevView, prevPosition, nextView, nextPosition ->
            nextView.setShowDivider(true)
            when(nextPosition){
                0 -> {
                    nextView.setWidth(0)
                    nextView.setWeight(0.6f)
                    nextView.setMultiSelect(true)
                    nextView.setBackgroundResource(R.color.white_f3f5f7)
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
            ToastHelper.showToast("position = $position; data = ${data.displayName}")
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
            ToastHelper.showToast(result.toString())
        }
        linkedView?.setData(dongList)
    }

    private fun clickView(view: View?){

    }

}