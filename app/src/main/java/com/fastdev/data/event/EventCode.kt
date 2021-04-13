package com.fastdev.data.event

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/13
 */
abstract class EventCode(
        var code: String? = "",
        var data: Any? = null) {

    override fun equals(other: Any?): Boolean {
        return if (other is EventCode) code == other.code else false
    }

    override fun hashCode(): Int {
        var result = code?.hashCode() ?: 0
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
    }

    companion object{
        val LOGIN = object : EventCode("login") {}
    }

}