package com.wyf.pictures.camera.capturedialog

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.view.View
import java.io.File
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import android.widget.Toast
import com.wyf.pictures.R
import com.wyf.pictures.camera.OnPicturePathListener
import com.wyf.pictures.camera.PictureCapture
import com.wyf.pictures.camera.helper.PermissionHelper
import com.wyf.pictures.camera.helper.PictureHelper
import com.wyf.pictures.camera.utils.PATH
import com.wyf.pictures.camera.utils.Photo
import com.wyf.pictures.camera.utils.PictureType
import com.wyf.pictures.rxPermissions.RxPermissions
import kotlinx.android.synthetic.main.picture_capture_dialog.*

/**
 * 选择相机或相册的弹窗，同时处理权限申请以及拍照或选择照片结果
 */
class PictureCaptureDialog : AppCompatActivity() {

    private var cropWith = 480
    private var cropHeiht = 480
    private val photo by lazy { Photo() }

    companion object {
        lateinit var listener: OnPicturePathListener
        fun setOnPicturePathListener(listener1: OnPicturePathListener) {
            listener = listener1
        }
    }

    private val temPictureFile by lazy {
        File(PATH.imageSaveDir + "camera-photo-" + System.currentTimeMillis() + ".jpg")
    }

    private val pictureFile by lazy {
        File(PATH.imageSaveDir + "crop-photo-" + System.currentTimeMillis() + ".jpg")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxPermissions.getInstance(this).request(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe {
                    if (it) { //有权限
                        setContentView(R.layout.picture_capture_dialog)
                        setWidgetClick()
                        setLayoutParams()
                        initResource()
                        initWidget()
                    } else {
                        Toast.makeText(this, getString(R.string.please_turn_on_camera_permissions), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
    }

    private fun initResource() {
        PATH.initialize(this)
    }

    private fun initWidget() {

    }

    private fun setLayoutParams() {
        var width = resources.displayMetrics.widthPixels
        var height = resources.displayMetrics.heightPixels
        var params = FrameLayout.LayoutParams(width, height)
        layout.layoutParams = params
    }

    private fun setWidgetClick() {
        Camera_btn.setOnClickListener {
            takePicture()
            setVisible()
        }

        Album_btn.setOnClickListener {
            getPictureFromAlbum()
            setVisible()
        }
        Select_Cancel.setOnClickListener { finish() }
    }

    private fun setVisible() {
        layout.visibility = View.INVISIBLE
    }

    private fun requestPermission() {
        RxPermissions.getInstance(this).request(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe {
                    if (it) { //有权限
                        layout.visibility = View.VISIBLE
                    } else {

                    }
                }
    }

    /**
     * 打开相机拍照获取图片
     */
    private fun takePicture() {
        val noPermissions = PermissionHelper.checkSelfPermission(this, arrayOf(android.Manifest.permission.CAMERA))
        if (noPermissions.isEmpty()) {
            PictureHelper.takePicture(this, temPictureFile)
        } else {
            requestPermission()
        }
    }

    /**
     * 从相册获取图片
     */
    private fun getPictureFromAlbum() {
        val noPermissions = PermissionHelper.checkSelfPermission(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))
        if (noPermissions.isEmpty()) {
            PictureHelper.openAlbum(this)
        } else {
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            val result = PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (result) {
                when (requestCode) {
                    PermissionHelper.REQUESTCODE_CAMERA -> takePicture()
                    PermissionHelper.REQUESTCODE_ALBUM -> getPictureFromAlbum()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    PictureHelper.REQUESTCODE_CAMERA -> {
                        dealWithCameraPicture()
                    }
                    PictureHelper.REQUESTCODE_ALBUM -> {
                        dealWithAlbumPicture(data)
                    }
                    PictureHelper.REQUESTCODE_CROP -> {
                        dealWithCropPicture(data)
                    }
                }
            } else {
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 处理拍照的结果
     */
    private fun dealWithCameraPicture() {
        try {
            val desUri: Uri = Uri.fromFile(pictureFile)
            val imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this, this.packageName + ".provider", temPictureFile)
            } else {
                Uri.fromFile(temPictureFile)
            }
            photo.originalFile = File(temPictureFile.toString())
            photo.pictureType = PictureType.CAMERA
            if (PictureCapture.needCrop) {
                PictureHelper.cropImageUri(this, imageUri, desUri, 1, 1, cropWith, cropHeiht)
            } else {
                sendImagePath(null, PictureType.CAMERA)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 处理从相册选择照片的结果
     */
    private fun dealWithAlbumPicture(data: Intent?) {
        try {
            val pictureUri: Uri = data!!.data
            val desUri: Uri = Uri.fromFile(pictureFile)
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(pictureUri,
                    filePathColumn, null, null, null)//从系统表中查询指定Uri对应的照片
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val path = cursor.getString(columnIndex)  //获取照片路径
            cursor.close()
            photo.originalFile = File(path)
            photo.pictureType = PictureType.ALBUM
            if (PictureCapture.needCrop) {
                PictureHelper.cropImageUri(this, pictureUri, desUri, 1, 1, cropWith, cropHeiht)
            } else {
                sendImagePath(Uri.parse(path), PictureType.ALBUM)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 处理裁剪后的图片并返回bitmap
     */
    private fun dealWithCropPicture(data: Intent?) {
        try {
            val uri: Uri? = if (data != null && data.data != null) {
                data.data
            } else {
                Uri.fromFile(pictureFile)
            }
            if (uri != null) {
                sendImagePath(uri, PictureType.CROP)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendImagePath(uri: Uri?, type: PictureType) {
        try {
            when (type) {
                PictureType.CROP -> {
                    val path = uri.toString().substring(uri.toString().indexOf("/") + 1, uri.toString().lastIndex + 1)
                    photo.cropFile = File(path)
                    photo.pictureType = PictureType.CROP
                }
                PictureType.CAMERA -> {
                    val path = temPictureFile.toString()
                }
                PictureType.ALBUM -> {
                    val path = uri.toString()
                }
            }
            listener.onPhoto(photo)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}