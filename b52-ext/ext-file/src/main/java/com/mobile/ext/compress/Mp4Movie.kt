package com.mobile.ext.compress

import android.annotation.TargetApi
import android.media.MediaCodec
import android.media.MediaFormat
import com.googlecode.mp4parser.util.Matrix
import java.io.File
import java.util.*

@TargetApi(16)
class Mp4Movie {
    var matrix: Matrix = Matrix.ROTATE_0
        private set
    val tracks = ArrayList<Track>()
    var cacheFile: File? = null
    var width = 0
        private set
    var height = 0
        private set

    fun setRotation(angle: Int) {
        when (angle) {
            0 -> {
                matrix = Matrix.ROTATE_0
            }
            90 -> {
                matrix = Matrix.ROTATE_90
            }
            180 -> {
                matrix = Matrix.ROTATE_180
            }
            270 -> {
                matrix = Matrix.ROTATE_270
            }
        }
    }

    fun setSize(w: Int, h: Int) {
        width = w
        height = h
    }

    @Throws(Exception::class)
    fun addSample(trackIndex: Int, offset: Long, bufferInfo: MediaCodec.BufferInfo?) {
        if (trackIndex < 0 || trackIndex >= tracks.size) {
            return
        }
        val track = tracks[trackIndex]
        track.addSample(offset, bufferInfo)
    }

    @Throws(Exception::class)
    fun addTrack(mediaFormat: MediaFormat?, isAudio: Boolean): Int {
        tracks.add(Track(tracks.size, mediaFormat, isAudio))
        return tracks.size - 1
    }
}