package com.mobile.ext.floating

import android.graphics.Point
import android.view.View
import androidx.annotation.LayoutRes

class FloatingParameter(
    @LayoutRes var layoutId: Int,
    var layoutView: View,
    var tag: String,
    var isDraggable: Boolean,
    var isDragging: Boolean,
    var isAnimating: Boolean,
    var isShowing: Boolean,
    var hasEditText: Boolean,
    var sliding: Sliding,
    var showing: Showing,
    var isWidthMatchParent: Boolean,
    var isHeightMatchParent: Boolean,
    var gravity: Int,
    var locationOffset: Point,
    var location: Point
) {

    class Builder() {
    }
}