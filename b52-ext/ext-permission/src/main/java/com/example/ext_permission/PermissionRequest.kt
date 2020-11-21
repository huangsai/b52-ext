package com.example.ext_permission

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager

object PermissionRequest {
    private const val TAG = "InvisibleFragment"
    fun request(
        fragmentManager: FragmentManager,
        permission: Array<String>,
        callback: PermissionCallback
    ) {
        val existedFragment = fragmentManager.findFragmentByTag(TAG)
        val fragment = if (existedFragment != null) {
            existedFragment as InvisibleFragment
        } else {
            val invisibleFragment = InvisibleFragment()
            fragmentManager.beginTransaction().add(invisibleFragment, TAG).commitNow()
            invisibleFragment
        }
        fragment.requestNow(callback, permission)
    }

    fun alertPermission(@StringRes msgRes: Int, activity: Activity?) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle("温馨提示")
                .setMessage(msgRes)
                .setPositiveButton("确定") { dialog, which ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", it.packageName, null)
                    intent.data = uri
                    activity.startActivity(intent)
                    dialog.dismiss()
                }
                .create()
                .show()
        }

    }

}