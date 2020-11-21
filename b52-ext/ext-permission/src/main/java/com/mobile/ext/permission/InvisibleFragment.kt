package com.mobile.ext.permission

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class InvisibleFragment : Fragment() {

    private var callback: PermissionCallback? = null

    fun requestNow(cb: PermissionCallback, permission: Array<String>) {
        callback = cb
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  //>=6.0
            var granted = 0
            for (i in permission) {
                granted += ContextCompat.checkSelfPermission(requireActivity(), i)
            }
            if (granted
                == PackageManager.PERMISSION_GRANTED
            ) {
                callback?.passPermissions()
            } else {
                requestPermissions(
                    permission,
                    1
                )
            }
        } else {
            callback?.passPermissions()
        }
    }

    /**
     * 请求返回结果
     * @param requestCode Int 请求码
     * @param permissions Array<String> 权限
     * @param grantResults IntArray 请求结果
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            callback?.onRequestPermissions(permissions, grantResults)
        }
    }


}