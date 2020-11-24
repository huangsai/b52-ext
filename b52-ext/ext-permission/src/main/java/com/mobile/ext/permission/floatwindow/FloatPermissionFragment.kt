package com.mobile.ext.permission.floatwindow

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


/**
 * 用于浮窗权限的申请，自动处理回调结果
 *
 */
internal class FloatPermissionFragment : Fragment() {

    companion object {
        private var onPermissionResult: OnPermissionResult? = null

        fun requestPermission(activity: FragmentActivity, onPermissionResult: OnPermissionResult) {
            Companion.onPermissionResult = onPermissionResult
            activity.supportFragmentManager
                    .beginTransaction().add(FloatPermissionFragment(), activity.localClassName)
                    .commitAllowingStateLoss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        FloatPermissionUtils.requestPermission(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FloatPermissionUtils.requestCode) {
            // 需要延迟执行，不然即使授权，仍有部分机型获取不到权限
            Handler(Looper.getMainLooper()).postDelayed({
                val activity = activity ?: return@postDelayed
                val check = FloatPermissionUtils.checkPermission(activity)
                // 回调权限结果
                onPermissionResult?.permissionResult(check)
                // 将Fragment移除
                activity.supportFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
            }, 500)
        }
    }

}