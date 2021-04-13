package com.fastdev.helper

import android.content.Context
import android.os.Build
import android.text.TextUtils
import androidx.fragment.app.FragmentActivity
import com.baselib.helper.RequestPermissionHelper
import com.baselib.helper.ToastHelper
import com.fastdev.ui.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.pingerx.socialgo.core.SocialGo
import com.pingerx.socialgo.core.SocialGoConfig
import com.pingerx.socialgo.core.adapter.IJsonAdapter
import com.pingerx.socialgo.core.adapter.impl.DefaultRequestAdapter
import com.pingerx.socialgo.core.exception.SocialError
import com.pingerx.socialgo.core.model.ShareEntity
import com.pingerx.socialgo.core.platform.Target
import com.pingerx.socialgo.wechat.WxPlatform
import okhttp3.*
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 功能描述:
 * @author tangdexiang
 * @since 2021/4/11
 */
object ShareSdk {
    val ACTION_H5 = "com.social.startaction"
    val thumbImageDefalut = "https://defalut.png"
    fun init(context: Context?){
        if(context == null) return
        if(SocialGo.isInitSDK()) return

        val config = SocialGoConfig.create(context)
                .debug(true)
                .defImageResId(R.mipmap.ic_launcher)
                .startAction(ACTION_H5)//微信h5拉起App发送广播Action
                .wechat("", "1232323")

        SocialGo
                .init(config)
                .registerWxPlatform(WxPlatform.Creator())
                .setJsonAdapter(GsonJsonAdapter())
                .setRequestAdapter(OkHttpRequestAdapter())
    }

    private fun share(activity: FragmentActivity?, platformType: Int, shareBean: ShareEntity?){
        if(shareBean == null) return
        if(activity == null) return
//        val progressDialog = if(activity is BaseActivity) activity.progressBar else DialogUtil.createProgressDialog(activity, "正在加载...", true)
        init(activity)

        SocialGo.doShare(activity, platformType, shareBean) {
            onStart { _, _ ->
//                progressDialog?.show()
            }
            onSuccess {
//                progressDialog?.dismiss()
            }
            onFailure {
//                progressDialog?.dismiss()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (it.errorCode == SocialError.CODE_STORAGE_READ_ERROR) {
                        RequestPermissionHelper.requestStoragePermission(activity, null)
                    } else if (it.errorCode == SocialError.CODE_STORAGE_WRITE_ERROR) {
                        RequestPermissionHelper.requestStoragePermission(activity, null)
                    }else{
                        ToastHelper.showToast(it.toString())
                    }
                }else{
                    ToastHelper.showToast(it.toString())
                }
            }
            onCancel {
//                progressDialog?.dismiss()
            }
        }
    }


    fun shareWeb2WechatFriends(activity: FragmentActivity?, title: String?, content: String?, url: String?, thumbImage: String?){
        val bean = ShareEntity.buildWebObj(title?: "", content?: "", if(TextUtils.isEmpty(thumbImage)) thumbImageDefalut else thumbImage?: thumbImageDefalut, url?: "")
        share(activity, Target.SHARE_WX_FRIENDS, bean)
    }

    fun shareWeb2WechatZone(activity: FragmentActivity?, title: String?, content: String?, url: String?, thumbImage: String?){
        val bean = ShareEntity.buildWebObj(title?: "", content?: "", if(TextUtils.isEmpty(thumbImage)) thumbImageDefalut else thumbImage?: thumbImageDefalut, url?: "")
        share(activity, Target.SHARE_WX_ZONE, bean)
    }

     fun shareImage2WechatZone(activity: FragmentActivity?, path: String?){
        val bean = ShareEntity.buildImageObj(path?: "")
        share(activity, Target.SHARE_WX_ZONE, bean)
    }

    fun shareImage2WechatFriends(activity: FragmentActivity?, path: String?){
        val bean = ShareEntity.buildImageObj(path?: "")
        share(activity, Target.SHARE_WX_FRIENDS, bean)
    }


    private class GsonJsonAdapter : IJsonAdapter {
        override fun <T> fromJson(jsonString: String, cls: Class<T>): T? {
            var t: T? = null
            try {
                val gson = Gson()
                t = gson.fromJson(jsonString, cls)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return t
        }

        override fun toJson(any: Any?): String? {
            val gsonBuilder = GsonBuilder()
                    .registerTypeAdapter(Date::class.java, JsonSerializer<Date> { src, _, _ -> JsonPrimitive(src.time) }).setDateFormat(
                            DateFormat.LONG)
            return gsonBuilder.create().toJson(any)
        }
    }
    private class OkHttpRequestAdapter : DefaultRequestAdapter() {

        private val mOkHttpClient: OkHttpClient = buildOkHttpClient()

        private fun buildOkHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
            // 连接超时
            builder.connectTimeout((5 * 1000).toLong(), TimeUnit.MILLISECONDS)
            // 读超时
            builder.readTimeout((5 * 1000).toLong(), TimeUnit.MILLISECONDS)
            // 写超时
            builder.writeTimeout((5 * 1000).toLong(), TimeUnit.MILLISECONDS)
            // 失败后重试
            builder.retryOnConnectionFailure(true)
            return builder.build()
        }


        // 借助 open api 提交图片
        override fun postData(url: String, params: Map<String, String>, fileKey: String, filePath: String): String? {
            val builder = MultipartBody.Builder()
            if (!TextUtils.isEmpty(filePath)) {
                val file = File(filePath)
                val body = RequestBody.create(MediaType.parse("image/*"), file)
                builder.addFormDataPart(fileKey, file.name, body)
                builder.setType(MultipartBody.FORM)
                for (key in params.keys) {
                    val value = params[key]
                    if (value != null) {
                        builder.addFormDataPart(key, value)
                    }
                }
                val multipartBody = builder.build()
                val request = Request.Builder().url(url).post(multipartBody).build()
                return try {
                    val execute = mOkHttpClient.newCall(request).execute()
                    execute.body()?.string()
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }

            }
            return null
        }
    }
}