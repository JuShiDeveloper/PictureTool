package com.wyf.pictures.camera
import android.content.Context
import android.content.Intent
import com.wyf.pictures.camera.capturedialog.PictureCaptureDialog
import com.wyf.pictures.camera.utils.Photo
import com.wyf.pictures.compression.PictureCompression

/**
 * 图片获取（拍照或从相册）
 */
object PictureCapture {
    /*是否需要剪裁*/
    var needCrop: Boolean = false

    /**
     * 获取图片，通过拍照或从相册选择 （不经过压缩）
     * @param context
     * @param listener   未压缩的图片文件回掉接口
     * @param needCrop   是否需要剪裁
     */
    fun getPicture(context: Context, listener: OnPicturePathListener, needCrop: Boolean) {
        this.needCrop = needCrop
        PictureCaptureDialog.setOnPicturePathListener(listener)
        showDialog(context)
    }

    /**
     * 获取图片，通过拍照或从相册选择 （压缩过的图片）
     * @param context
     * @param listener   压缩过的图片文件回掉接口
     * @param needCrop   是否需要剪裁
     */
    fun getCompressionPicture(context: Context, listener: OnPicturePathListener, needCrop: Boolean) {
        this.needCrop = needCrop
        PictureCaptureDialog.setOnPicturePathListener(object : OnPicturePathListener {
            override fun onPhoto(photo: Photo) {
                PictureCompression.compressionPictureRx(context, photo, listener)
            }
        })
        showDialog(context)
    }

    /**
     *显示选择Camera 或 Album 的Dialog
     */
    private fun showDialog(context: Context) {
        var intent = Intent(context, PictureCaptureDialog::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

}