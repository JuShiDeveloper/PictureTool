package com.wyf.pictures.camera
import com.wyf.pictures.camera.utils.Photo

/**
 * 拍照或从相册选择图片回掉接口
 */
interface OnPicturePathListener {
    /**
     * @param photo  选择的图片文件对象（包含原始文件，压缩文件，裁剪文件）
     */
    fun onPhoto(photo: Photo)
}