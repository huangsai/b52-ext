package com.mobile.ext.compress

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * 视频压缩
 */
object VideoCompress {

    /**
     * 低质量压缩
     * @param srcPath 视频输入路径
     * @param destPath 视频输出路径
     * @param listener 压缩结果接口监听
     */
    fun compressVideoLow(
        srcPath: String,
        destPath: String,
        listener: VideoCompressListener
    ) {
        compressVideo(srcPath, destPath, VideoController.COMPRESS_QUALITY_LOW, listener)
    }

    /**
     * 中质量压缩
     */
    fun compressVideoMedium(
        srcPath: String,
        destPath: String,
        listener: VideoCompressListener
    ) {
        compressVideo(srcPath, destPath, VideoController.COMPRESS_QUALITY_MEDIUM, listener)
    }

    /**
     * 高质量压缩
     */
    fun compressVideoHigh(
        srcPath: String,
        destPath: String,
        listener: VideoCompressListener
    ) {
        compressVideo(srcPath, destPath, VideoController.COMPRESS_QUALITY_HIGH, listener)
    }

    private fun compressVideo(
        srcPath: String,
        destPath: String,
        quality: Int,
        listener: VideoCompressListener
    ) {
        listener.onStart()
        GlobalScope.launch(Dispatchers.IO) {
            val result = VideoController.getInstance()
                .convertVideo(srcPath, destPath, quality) { percent ->
                    listener.onProgress(percent)
                }
            withContext(Dispatchers.Main) {
                if (result) {
                    listener.onSuccess(VideoController.cachedFile)
                } else {
                    listener.onFail()
                }
            }
        }
    }

    interface VideoCompressListener {
        fun onStart()
        fun onSuccess(file: File?)
        fun onFail()
        fun onProgress(percent: Float)
    }
}