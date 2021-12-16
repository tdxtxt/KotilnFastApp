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
        val roomList1 = mutableListOf(Place(1101, "101房间"), Place(1102, "102房间"), Place(1103, "103")
                , Place(1104, "104房间"), Place(1105, "105房间"))

        val roomList2 = mutableListOf(Place(1201, "201房间"), Place(1202, "202房间"), Place(1203, "203")
                , Place(1204, "204房间"), Place(1205, "205房间"))

        val roomList3 = mutableListOf(Place(2101, "101房间"), Place(2102, "102房间"), Place(2103, "103")
                , Place(2104, "104房间"), Place(2105, "105房间"))

        val roomList4 = mutableListOf(Place(2201, "201房间"), Place(2202, "202房间"), Place(2203, "203")
                , Place(2204, "204房间"), Place(2205, "205房间"))

        val foorList1 = mutableListOf(Place(11, "1楼", roomList1), Place(12, "2楼", roomList2))

        val foorList2 = mutableListOf(Place(21, "1楼", roomList3), Place(22, "2楼", roomList4))

        val dongList = mutableListOf(Place(1, "1栋", foorList1), Place(2, "2栋", foorList2))

        adapterDong = object : BaseQuickAdapter<Place, BaseViewHolder>(R.layout.item_dong){
            override fun convert(holder: BaseViewHolder, item: Place) {
                holder.setText(R.id.tv_name, item.name)
            }
        }.apply {
            setOnItemClickListener { adapter, view, position ->
                adapterFoor?.setNewInstance(getItem(position).childs)
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