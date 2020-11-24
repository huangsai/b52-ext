package com.mobile.ext.permission

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionRequest {

    private var callback: PermissionCallback? = null

    fun request(
        activity: Activity,
        requestCode: Int,
        permission: Array<String>,
        cb: PermissionCallback
    ) {
        callback = cb
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  //>=6.0
            var granted = 0
            for (i in permission) {
                granted += ContextCompat.checkSelfPermission(activity, i)
            }
            if (granted
                == PackageManager.PERMISSION_GRANTED
            ) {
                callback?.passPermissions()
            } else {
                activity.requestPermissions(
                    permission,
                    requestCode
                )
            }
        } else {
            callback?.passPermissions()
        }
    }

    fun request(
        fragment: Fragment,
        requestCode: Int,
        permission: Array<String>,
        cb: PermissionCallback
    ) {
        callback = cb
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  //>=6.0
            var granted = 0
            for (i in permission) {
                granted += ContextCompat.checkSelfPermission(fragment.requireActivity(), i)
            }
            if (granted
                == PackageManager.PERMISSION_GRANTED
            ) {
                callback?.passPermissions()
            } else {
                fragment.requestPermissions(
                    permission,
                    requestCode
                )
            }
        } else {
            callback?.passPermissions()
        }
    }

    fun alertPermission(@StringRes msgRes: Int, activity: Activity?) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.base_alert_title)
                .setMessage(msgRes)
                .setPositiveButton(R.string.base_alert_known) { dialog, which ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", it.packageName, null)
                    intent.data = uri
                    it.startActivity(intent)
                    dialog.dismiss()
                }
                .create()
                .show()
        }

    }

}