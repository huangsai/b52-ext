package com.mobile.ext.exo

import android.content.res.Resources
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.trackselection.MappingTrackSelector

const val TAG_EXO_PLAYER = "ExoPlayer"

fun isSupportedRenderer(
    mappedTrackInfo: MappingTrackSelector.MappedTrackInfo,
    rendererIndex: Int
): Boolean {
    val trackGroupArray = mappedTrackInfo.getTrackGroups(rendererIndex)
    if (trackGroupArray.length == 0) {
        return false
    }
    val trackType = mappedTrackInfo.getRendererType(rendererIndex)
    return isSupportedTrackType(trackType)
}

fun isSupportedTrackType(trackType: Int): Boolean {
    return when (trackType) {
        C.TRACK_TYPE_VIDEO, C.TRACK_TYPE_AUDIO, C.TRACK_TYPE_TEXT -> true
        else -> false
    }
}

fun getTrackTypeString(resources: Resources, trackType: Int): String {
    return when (trackType) {
        C.TRACK_TYPE_VIDEO -> resources.getString(R.string.exo_track_selection_title_video)
        C.TRACK_TYPE_AUDIO -> resources.getString(R.string.exo_track_selection_title_audio)
        C.TRACK_TYPE_TEXT -> resources.getString(R.string.exo_track_selection_title_text)
        else -> throw IllegalArgumentException()
    }
}