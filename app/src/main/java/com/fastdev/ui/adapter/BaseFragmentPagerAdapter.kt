package com.fastdev.ui.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/31
 */
class BaseFragmentPagerAdapter constructor(data: List<Pair<String, Fragment>>, val fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    var mFragmentList: MutableList<Fragment> = mutableListOf()
    private var titles: MutableList<String> = mutableListOf()
    private var mFragmentPositionMap: SparseArray<String> = SparseArray()
    private var mFragmentPositionMapAfterUpdate: SparseArray<String> = SparseArray()

    constructor(data: MutableList<Fragment>, fragmentManager: FragmentManager, str: String = "") : this(data.map { Pair(str, it) }, fragmentManager)

    init {
        data.forEach {
            mFragmentList.add(it.second)
            titles.add(it.first)
        }
        setFragmentPositionMap()
        setFragmentPositionMapForUpdate()
    }

    override fun getItemPosition(obj: Any): Int {
        val hashCode: Int = obj.hashCode()
        //查找object在更新后的列表中的位置
        //查找object在更新后的列表中的位置
        val position = mFragmentPositionMapAfterUpdate[hashCode]
        //更新后的列表中不存在该object的位置了
        //更新后的列表中不存在该object的位置了
        if (position == null) {
            return POSITION_NONE
        } else {
            //如果更新后的列表中存在该object的位置, 查找该object之前的位置并判断位置是否发生了变化
            val size = mFragmentPositionMap.size()
            for (i in 0 until size) {
                val key = mFragmentPositionMap.keyAt(i)
                if (key == hashCode) {
                    val index = mFragmentPositionMap[key]
                    return if (position == index) {
                        //位置没变依然返回POSITION_UNCHANGED
                        POSITION_UNCHANGED
                    } else {
                        //位置变了
                        POSITION_NONE
                    }
                }
            }
        }
        return POSITION_UNCHANGED
    }

    /**
     * 此方法不用position做返回值即可破解fragment tag异常的错误
     */
    override fun getItemId(position: Int): Long {
        // 获取当前数据的hashCode，其实这里不用hashCode用自定义的可以关联当前Item对象的唯一值也可以，只要不是直接返回position
        return mFragmentList[position].hashCode().toLong()
    }

    override fun getItem(position: Int) = mFragmentList.get(position)

    override fun getCount() = mFragmentList.size

    override fun getPageTitle(position: Int): CharSequence? {
        return if (titles != null) titles[position] else super.getPageTitle(position)
    }

    /**
     * 保存更新之前的位置信息，用<hashCode></hashCode>, position>的键值对结构来保存
     */
    private fun setFragmentPositionMap() {
        mFragmentPositionMap.clear()
        for (i in mFragmentList.indices) {
            mFragmentPositionMap.put(java.lang.Long.valueOf(getItemId(i)).toInt(), i.toString())
        }
    }

    /**
     * 保存更新之后的位置信息，用<hashCode></hashCode>, position>的键值对结构来保存
     */
    private fun setFragmentPositionMapForUpdate() {
        mFragmentPositionMapAfterUpdate.clear()
        for (i in mFragmentList.indices) {
            mFragmentPositionMapAfterUpdate.put(
                java.lang.Long.valueOf(getItemId(i)).toInt(),
                i.toString()
            )
        }
    }

    /**
     * 添加Fragment
     * @param fragment 目标Fragment
     */
    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
        notifyItemChanged()
    }
    /**
     * 将指定的Fragment替换/更新为新的Fragment
     * @param oldFragment 旧Fragment
     * @param newFragment 新Fragment
     */
    fun replaceFragment(oldFragment: Fragment, newFragment: Fragment) {
        val position = mFragmentList.indexOf(oldFragment)
        if (position == -1) {
            return
        }
        //从Transaction移除旧的Fragment
        removeFragmentInternal(oldFragment)
        //替换List中对应的Fragment
        mFragmentList[position] = newFragment
        //刷新Adapter
        notifyItemChanged()
    }
    /**
     * 将指定位置的Fragment替换/更新为新的Fragment，同{@link #replaceFragment(BaseFragment oldFragment, BaseFragment newFragment)}
     * @param position    旧Fragment的位置
     * @param newFragment 新Fragment
     */
    fun replaceFragment(position: Int, newFragment: Fragment) {
        val oldFragment: Fragment = mFragmentList[position]
        removeFragmentInternal(oldFragment)
        mFragmentList[position] = newFragment
        notifyItemChanged()
    }

    /**
     * 移除指定的Fragment
     * @param fragment 目标Fragment
     */
    fun removeFragment(fragment: Fragment) {
        //先从List中移除
        mFragmentList.remove(fragment)
        //然后从Transaction移除
        removeFragmentInternal(fragment)
        //最后刷新Adapter
        notifyItemChanged()
    }

    /**
     * 从Transaction移除Fragment
     * @param fragment 目标Fragment
     */
    private fun removeFragmentInternal(fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.remove(fragment)
        transaction.commitNow()
    }

    private fun notifyItemChanged() {
        //刷新之前重新收集位置信息
        setFragmentPositionMapForUpdate()
        notifyDataSetChanged()
        setFragmentPositionMap()
    }

}