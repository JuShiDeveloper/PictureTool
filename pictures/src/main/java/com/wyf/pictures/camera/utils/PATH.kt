package com.wyf.pictures.camera.utils

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
/**
 * 相关路径信息类
 *
 * @author wyf
 */
object PATH {

    fun initialize(context: Context) {
        mContext = context
        FILE.createDir(imageSaveDir)
    }

    private lateinit var mContext: Context

    /**
     * 图片另存为SDCard/Android/data/目录
     *
     * @return
     */
    val imageSaveDir: String
        get() = mContext.getExternalFilesDir(null).toString() + "/photo/"

}
