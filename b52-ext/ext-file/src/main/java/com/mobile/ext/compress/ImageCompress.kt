package com.mobile.ext.compress

import android.net.Uri
import android.text.TextUtils
import com.mobile.guava.android.mvvm.AndroidX
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File


/**
 * 图片压缩
 */
object ImageCompress {

    /**
     * 根据file压缩
     * @param file 压缩图片文件
     * @param targetPath 压缩后图片缓存路径
     * @param compressListener 压缩结果接口监听
     */
    fun compressByFile(file: File, targetPath: String, compressListener: OnCompressListener) {
        Luban.with(AndroidX.myApp)
            .load(file)
            .ignoreBy(100) //不压缩的阈值，单位为K
            .setTargetDir(targetPath)
            .filter { !(TextUtils.isEmpty(it)) }
            .setCompressListener(compressListener).launch()
    }

    /**
     * 根据path压缩
     * @param path 压缩图片路径
     * @param targetPath 压缩后图片缓存路径
     * @param compressListener 压缩结果接口监听
     */
    fun compressByPath(path: String, targetPath: String, compressListener: OnCompressListener) {
        Luban.with(AndroidX.myApp)
            .load(path)
            .ignoreBy(100) //不压缩的阈值，单位为K
            .setTargetDir(targetPath)
            .filter { !(TextUtils.isEmpty(it)) }
            .setCompressListener(compressListener).launch()
    }

    /**
     * 根据Uri压缩
     * @param uri 压缩图片Uri
     * @param targetPath 压缩后图片缓存路径
     * @param compressListener 压缩结果接口监听
     */
    fun compressByUri(uri: Uri, targetPath: String, compressListener: OnCompressListener) {
        Luban.with(AndroidX.myApp)
            .load(uri)
            .ignoreBy(100) //不压缩的阈值，单位为K
            .setTargetDir(targetPath)
            .filter { !(TextUtils.isEmpty(it)) }
            .setCompressListener(compressListener).launch()
    }

    /**
     * 根据Uri压缩
     * @param files 压缩图片集合
     * @param targetPath 压缩后图片缓存路径
     * @param compressListener 压缩结果接口监听
     */
    fun compressList(files: List<Any>, targetPath: String, compressListener: OnCompressListener) {
        Luban.with(AndroidX.myApp)
            .load(files)
            .ignoreBy(100) //不压缩的阈值，单位为K
            .setTargetDir(targetPath)
            .filter { !(TextUtils.isEmpty(it)) }
            .setCompressListener(compressListener).launch()
    }
}