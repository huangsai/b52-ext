package com.example.ext_permission

interface PermissionCallback {
    fun passPermissions()
    fun onRequestPermissions(permissions: Array<String>,
                             grantResults: IntArray)
}