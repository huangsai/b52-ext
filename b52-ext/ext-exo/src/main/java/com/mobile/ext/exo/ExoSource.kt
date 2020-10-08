package com.mobile.ext.exo

import com.google.android.exoplayer2.source.MediaSource

data class ExoSource(
    val position: Long,
    val windowIndex: Int,
    val mediaSources: List<MediaSource>
)