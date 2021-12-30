package com.fastdev.data.response

import cn.wzbos.android.widget.linked.IPickerData

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/16
 */
class PlaceBean : IPickerData {
    constructor(checked: Boolean, id: String?, parentId: String?, name: String?, stub: MutableList<PlaceBean>?) {
        this.checked = checked
        this.id = id
        this.parentId = parentId
        this.name = name
        this.stub = stub
    }

    constructor(id: String?, name: String?) : this(false, id, null,  name, null)

    constructor(id: String?, name: String?, stub: MutableList<PlaceBean>) : this(false, id, null,  name, stub.apply { forEach { it.parentId = id } })

    var selected: Boolean = false
    var checked: Boolean = false
    var id: String? = ""
    var grandparentId: String? = null
    var parentId: String? = null
    var name: String? = ""
    var stub: MutableList<PlaceBean>? = null
    override fun isSelectedItem(): Boolean {
        return selected
    }

    override fun setSelectItem(selected: Boolean) {
        this.selected = selected
    }

    override fun setCheckedItem(checked: Boolean) {
        this.checked = checked
    }

    override fun isCheckedItem(): Boolean {
        return checked
    }

    override fun getObjId(): String {
        return id.toString()
    }

    override fun getDisplayName(): String {
        return name?: ""
    }

    override fun nodes(): MutableList<out IPickerData>? {
        return stub
    }

    override fun toString(): String {
        return name?:""
    }
}