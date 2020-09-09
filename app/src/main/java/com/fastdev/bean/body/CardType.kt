package com.fastdev.bean.body

import org.jaaksi.pickerview.dataset.OptionDataSet

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/8/11
 */
class CardType constructor(val name: String, val number:String) : OptionDataSet {

    override fun getCharSequence() = name

    override fun getValue() = number

    override fun getSubs(): MutableList<out OptionDataSet>? {
        return null
    }

    companion object{
        fun getCardTypes() = mutableListOf(
                CardType("身份证", "1"),
                CardType("户口本", "2"),
                CardType("军官证", "3"),
                CardType("警官证", "4"),
                CardType("士兵证", "5"),
                CardType("文职干部证", "6"),
                CardType("护照", "7"),
                CardType("港澳台同胞证", "8"),
                CardType("其他", "9")
        )
    }
}