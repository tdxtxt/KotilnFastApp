package com.fastdev.ui.dialog

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.baselib.ui.dialog.BottomBaseDialog
import com.baselib.ui.dialog.impl.IBDialog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fastdev.data.response.Place
import com.fastdev.ui.R

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/16
 */
class SourceFilterDialog constructor(activity: FragmentActivity) : BottomBaseDialog(activity) {

    var recyclerDong: RecyclerView? = null
    var recyclerFoor: RecyclerView? = null
    var recyclerRoom: RecyclerView? = null

    var adapterDong: BaseQuickAdapter<Place, BaseViewHolder>? = null
    var adapterFoor: BaseQuickAdapter<Place, BaseViewHolder>? = null
    var adapterRoom: BaseQuickAdapter<Place, BaseViewHolder>? = null


    override fun getLayoutId() = R.layout.dialog_source_filter

    override fun onCreate(dialog: IBDialog) {
        recyclerDong = findViewById(R.id.recyclerDong)
        recyclerFoor = findViewById(R.id.recyclerFoor)
        recyclerRoom = findViewById(R.id.recyclerRoom)
        val roomList1 = mutableListOf(Place(1101, "1栋1楼101房间"), Place(1102, "1栋1楼102房间"), Place(1103, "1栋1楼103房间")
                , Place(1104, "1栋1楼104房间"), Place(1105, "1栋1楼105房间"))

        val roomList2 = mutableListOf(Place(1201, "1栋2楼201房间"), Place(1202, "1栋2楼202房间"), Place(1203, "1栋2楼203房间")
                , Place(1204, "204房间"), Place(1205, "1栋2楼205房间"))

        val roomList3 = mutableListOf(Place(2101, "2栋1楼101房间"), Place(2102, "2栋1楼102房间"), Place(2103, "2栋1楼103房间")
                , Place(2104, "2栋1楼104房间"), Place(2105, "2栋1楼105房间"))

        val roomList4 = mutableListOf(Place(2201, "2栋2楼201房间"), Place(2202, "2栋2楼202房间"), Place(2203, "2栋2楼203房间")
                , Place(2204, "204房间"), Place(2205, "2栋2楼205房间"))

        val foorList1 = mutableListOf(Place(11, "1栋1楼", roomList1), Place(12, "1栋2楼", roomList2))

        val foorList2 = mutableListOf(Place(21, "2栋1楼", roomList3), Place(22, "2栋2楼", roomList4))

        val foorList3 = mutableListOf(Place(31, "3栋1楼"), Place(32, "3栋2楼"))

        val dongList = mutableListOf(Place(1, "1栋", foorList1), Place(2, "2栋", foorList2), Place(1, "3栋", foorList3))

        adapterDong = object : BaseQuickAdapter<Place, BaseViewHolder>(R.layout.item_dong){
            override fun convert(holder: BaseViewHolder, item: Place) {
                holder.setText(R.id.tv_name, item.name)
                holder.itemView.isSelected = item.checked
            }
        }.apply {
            setOnItemClickListener { adapter, view, position ->
                data.forEachIndexed { index, place ->
                    data[index].checked = index == position
                }
                notifyDataSetChanged()
                adapterFoor?.setNewInstance(getItem(position).childs)
                adapterRoom?.setNewInstance(null)
            }

            recyclerDong?.adapter = this
        }
        adapterFoor = object : BaseQuickAdapter<Place, BaseViewHolder>(R.layout.item_foor) {
            override fun convert(holder: BaseViewHolder, item: Place) {
                holder.setText(R.id.tv_name, item.name)
            }
        }.apply {
            setOnItemClickListener { adapter, view, position ->
                adapterRoom?.setNewInstance(getItem(position).childs)
            }
            recyclerFoor?.adapter = this
        }

        adapterRoom = object : BaseQuickAdapter<Place, BaseViewHolder>(R.layout.item_room) {
            override fun convert(holder: BaseViewHolder, item: Place) {
                holder.setText(R.id.tv_name, item.name)
            }
        }.apply {
            recyclerRoom?.adapter = this
        }

        adapterDong?.setNewInstance(dongList)
    }
}