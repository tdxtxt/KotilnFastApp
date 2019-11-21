package com.baselib.net.log

import android.text.TextUtils
import com.baselib.helper.LogA
import okhttp3.logging.HttpLoggingInterceptor

class HttpLogger : HttpLoggingInterceptor.Logger {
    private var mMessage = StringBuilder()

    override fun log(message: String) {
        //请求或者响应开始
        if(message.startsWith("--> POST"))
            mMessage.setLength(0)

        mMessage.append(message).append("\n")
        if(message.startsWith("<-- END HTTP"))
            LogA.d(mMessage.toString())
    }

    /**
     * 格式化json
     */
    private fun formatJson(json: String): String{
        if(TextUtils.isEmpty(json)) return ""
        var sb = StringBuilder()
        var last = '\u0000'
        var current = '\u0000'
        var indent = 0
        for (i in json){
            last = current
            current = i
            when(current){
                '{','[' ->{
                    sb.append(current).append('\n')
                    indent ++
                    addIndentBlank(sb, indent)
                }
                '}',']' ->{
                    sb.append('\n')
                    indent --
                    addIndentBlank(sb, indent)
                    sb.append(current)
                }
                ',' ->{
                    sb.append(current)
                    if(last != '\\'){
                        sb.append('\n')
                        addIndentBlank(sb, indent)
                    }
                }
                else -> sb.append(current)
            }
        }
        return sb.toString()
    }
    private fun addIndentBlank(sb: StringBuilder, indent: Int){
        for (i in 0..indent)
            sb.append('\t')
    }
}