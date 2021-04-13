package com.baselib.helper

import android.content.Context
import android.util.TypedValue

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2020/7/17
 */

fun Int.dp2px(context: Context?): Float {
    val scale = context?.resources?.displayMetrics?.density
    return (this * (scale?: 0f) + 0.5f)
}

fun Float.dp2px(context: Context?): Float {
    val scale = context?.resources?.displayMetrics?.density
    return (this * (scale?: 0f) + 0.5f)
}

fun Float.sp2px(context: Context?): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, context?.resources?.displayMetrics)
}

fun Int.sp2px(context: Context?): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), context?.resources?.displayMetrics)
}