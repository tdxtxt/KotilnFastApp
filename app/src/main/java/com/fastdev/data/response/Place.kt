package com.fastdev.data.response

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/12/16
 */
class Place {
    constructor(checked: Boolean, id: Int, parentId: Int?, name: String?, childs: MutableList<Place>?) {
        this.checked = checked
        this.id = id
        this.parentId = parentId
        this.name = name
        this.childs = childs
    }

    constructor(id: Int, name: String?) : this(false, id, null,  name, null)

    constructor(id: Int, name: String?, childs: MutableList<Place>) : this(false, id, null,  name, childs.apply { forEach { it.parentId = id } })

    var selected: Boolean = false
    var checked: Boolean = false

    var grandparentId: Int? = 0
    var parentId: Int? = null
    var id: Int = 0
    var name: String? = ""
    var childs: MutableList<Place>? = null
}