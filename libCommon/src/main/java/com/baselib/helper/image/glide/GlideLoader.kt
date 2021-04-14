package com.baselib.helper.image.glide

import android.annotation.SuppressLint
import android.widget.ImageView
import com.baselib.R
import com.baselib.helper.image.ILoader
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.io.File

/**
 * 创建时间： 2020/5/25
 * 编码： tangdex
 * 功能描述:
 */
object GlideLoader: ILoader {

  override fun init(){

  }

  override fun loadImage(view: ImageView?, resId: Int){
    view?.setImageResource(resId)
  }

  override fun loadImage(view: ImageView?, url: String) {
      loadImage(view, url, R.drawable.baselib_image_placeholder, true)
  }

  override fun loadImage(view: ImageView?, url: String, placeholderResId: Int) {
    loadImage(view, url, placeholderResId, true)
  }

  @SuppressLint("ResourceType")
  override fun loadImage(view: ImageView?, url: String,  placeholderResId: Int, isCache: Boolean) {
      if(view == null) return
      var requests = GlideApp.with(view.context)
      var request = requests.load(url)
      request.transition(DrawableTransitionOptions().crossFade())
      when{
          placeholderResId > 0 -> {
              request.placeholder(placeholderResId)
              request.error(placeholderResId)
          }
          !isCache ->{
              // 跳过内存缓存
              request.skipMemoryCache(true)
              // 跳过磁盘缓存
              request.diskCacheStrategy(DiskCacheStrategy.NONE)
          }
      }
      request.into(view)
  }

  override fun saveImage(
      url: String,
      destFile: File,
      callback: (isSuccess: Boolean, msg: String) -> Unit) {
     }


  override fun clearMemoryCache() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun clearDiskCache() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}