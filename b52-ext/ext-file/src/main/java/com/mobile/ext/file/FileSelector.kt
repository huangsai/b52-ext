package com.mobile.ext.file

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.mobile.guava.android.mvvm.AndroidX
import java.io.File

object FileSelector {

    private var cameraImageFile: File? = null

    fun FragmentActivity.registerPicResultCallback(resultCallback: ActivityResultCallback<ActivityResult>): ActivityResultLauncher<Intent>? {
        return registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            resultCallback
        )
    }

    fun FragmentActivity.registerAudioResultCallback(resultCallback: ActivityResultCallback<Uri>): ActivityResultLauncher<String>? {
        return registerForActivityResult(
            ActivityResultContracts.GetContent(),
            resultCallback
        )
    }

    fun FragmentActivity.registerFileResultCallback(resultCallback: ActivityResultCallback<Uri>): ActivityResultLauncher<String>? {
        return registerForActivityResult(
            ActivityResultContracts.GetContent(),
            resultCallback
        )
    }

    fun FragmentActivity.registerDocumentResultCallback(resultCallback: ActivityResultCallback<Uri>): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(
            ActivityResultContracts.OpenDocument(),
            resultCallback
        )
    }

    /**
     * 获取图片
     */
    fun selectPic(launcher: ActivityResultLauncher<Intent>?) {
        val intent = Intent().apply {
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            action = Intent.ACTION_PICK
        }
        launcher?.launch(intent)
    }

    /**
     * 拍照
     */
    fun takeCamera(context: Context, launcher: ActivityResultLauncher<Intent>?) {
        cameraImageFile = null
        val tempFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis().toString() + ".jpeg"
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(
                context,
                AndroidX.myApp.packageName,
                tempFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile))
        }
        cameraImageFile = tempFile
        launcher?.launch(intent)
    }

    fun getCameraPicUri(context: Context): Uri? {
        if (cameraImageFile == null) return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                AndroidX.myApp.packageName,
                cameraImageFile!!
            )
        } else {
            Uri.fromFile(cameraImageFile!!)
        }
    }

    fun getCameraPicFile(): File? {
        return cameraImageFile
    }

    /**
     * 获取音频
     */
    fun selectAudio(launcher: ActivityResultLauncher<String>?) {
        launcher?.launch("audio/*")
    }

    /**
     * 获取单个类型File
     * @param mimeType 文件类型
     */
    fun selectFile(mimeType: String, launcher: ActivityResultLauncher<String>?) {
        launcher?.launch(mimeType)
    }

    /**
     * 获取多种类型File
     * @param mimeTypeArray 文件类型数组
     */
    fun selectFile(mimeTypeArray: Array<String>, launcher: ActivityResultLauncher<Array<String>>?) {
        launcher?.launch(mimeTypeArray)
    }
}