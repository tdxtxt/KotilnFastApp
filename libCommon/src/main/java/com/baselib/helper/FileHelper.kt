package com.baselib.helper;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import com.baselib.app.ApplicationDelegate;
import java.io.File;

object FileHelper {
    /**
     * 不需要读写权限了
     */
    private fun getCacheDir(context: Context?, dirName: String): File {
        var context = context
        if (context == null) context = ApplicationDelegate.context

        var cacheDir: File?
        if (isSdcardExist()) {
            cacheDir = context?.externalCacheDir
            if (cacheDir == null) {
                cacheDir = File(Environment.getExternalStorageDirectory(),
                    "Android/data/" + context?.packageName + "/cache/" + dirName)
            }
            cacheDir = File(cacheDir, dirName)
            cacheDir.mkdirs()
        } else {
            cacheDir = File(context?.cacheDir, dirName)
        }
        //        if (cacheDir.exists() || cacheDir.mkdirs()) {
        //            return cacheDir;
        //        }
        return cacheDir
    }

    /**
     * 需要文件的写入权限
     */
    private fun getSdcardRootDir(context: Context): File? {
        var sdcardDir: File? = null
        if (isSdcardExist()) {
            sdcardDir = Environment.getExternalStorageDirectory()
        } else {
            sdcardDir = context.cacheDir
        }
        return sdcardDir
    }

    /**
     * sd卡是否存在
     */
    private fun isSdcardExist(): Boolean {
        //        getExternalStoragePublicDirectory
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * 图片存放目录位置
     */
    fun getImageDir(context: Context): File {
        return getCacheDir(context, "image")
    }

    /**
     * 需要文件的写入权限
     * @return SDCard--/DCIM
     */
    fun getDCIMDir(context: Context): File {
        val dcimDir = File(getSdcardRootDir(context), "DCIM")
        dcimDir.mkdirs()
        return dcimDir
    }

    fun getFileByPath(filePath: String): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    fun isDir(dirPath: String): Boolean {
        return isDir(getFileByPath(dirPath))
    }

    fun isDir(file: File?): Boolean {
        return file != null && file.exists() && file.isDirectory
    }

    fun isFile(filePath: String): Boolean {
        return isFile(getFileByPath(filePath))
    }

    fun isFile(file: File?): Boolean {
        return file != null && file.exists() && file.isFile
    }

    fun getSize(path: String): String {
        if (TextUtils.isEmpty(path)) return "null"
        return if (isDir(path)) getDirSize(path) else byte2FitMemorySize(
            if (getFileByPath(path) != null) getFileByPath(path)!!.length() else 0)
    }

    fun getSize(file: File?): String {
        if (file == null) return "null"
        return if (isDir(file)) getDirSize(file) else byte2FitMemorySize(file.length())
    }

    fun getDirSize(dirPath: String): String {
        return getDirSize(getFileByPath(dirPath))
    }

    fun getDirSize(dir: File?): String {
        var len = getDirLength(dir)
        return if (len == -1L) "" else byte2FitMemorySize(len)
    }

    fun getDirLength(dirPath: String): Long {
        return getDirLength(getFileByPath(dirPath))
    }

    fun getDirLength(dir: File?): Long {
        if (!isDir(dir)) return -1
        var len: Long = 0
        val files = dir!!.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (file in files) {
                if (file.isDirectory) {
                    len += getDirLength(file)
                } else {
                    len += file.length()
                }
            }
        }
        return len
    }

    private fun byte2FitMemorySize(byteNum: Long): String {
        return if (byteNum < 0) {
            "shouldn't be less than zero!"
        } else if (byteNum < 1024) {
            String.format("%.3fB", byteNum.toDouble())
        } else if (byteNum < 1048576) {
            String.format("%.3fKB", byteNum.toDouble() / 1024)
        } else if (byteNum < 1073741824) {
            String.format("%.3fMB", byteNum.toDouble() / 1048576)
        } else {
            String.format("%.3fGB", byteNum.toDouble() / 1073741824)
        }
    }

    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }
}
