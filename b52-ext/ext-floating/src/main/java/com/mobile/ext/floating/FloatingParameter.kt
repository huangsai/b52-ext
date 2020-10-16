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
    // 坐标的偏移量
    var offsetPair: Pair<Int, Int> = Pair(0, 0),

    // 固定的初始坐标，左上角坐标
    var locationPair: Pair<Int, Int> = Pair(0, 0),
    // ps：优先使用固定坐标，若固定坐标不为原点坐标，gravity属性和offset属性无效

    // Callbacks
    var invokeView: OnInvokeView? = null,
    var callbacks: OnFloatCallbacks? = null,

    // 通过Kotlin DSL设置回调，无需复写全部方法，按需复写
    var floatCallbacks: FloatCallbacks? = null,

    // 出入动画
    var floatAnimator: OnFloatAnimator? = DefaultAnimator(),
    var appFloatAnimator: OnAppFloatAnimator? = AppFloatDefaultAnimator(),

    // 设置屏幕的有效显示高度（不包含虚拟导航栏的高度），仅针对系统浮窗，一般不用复写
    var displayHeight: OnDisplayHeight = DefaultDisplayHeight(),

    // 不需要显示系统浮窗的页面集合，参数为类名
    val filterSet: MutableSet<String> = mutableSetOf(),

    // 是否设置，当前创建的页面也被过滤
    internal var filterSelf: Boolean = false,

    // 是否需要显示，当过滤信息匹配上时，该值为false（用户手动调用隐藏，该值也为false，相当于手动过滤）
    internal var needShow: Boolean = true


    class Builder() {
    }
}